using AutoMapper;
using ExpenseMangamentAPI.DTO.Users;
using ExpenseMangamentAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.OData.UriParser;
using System;

namespace ExpenseMangamentAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ExpensesController : ControllerBase
    {
        private readonly ExpenseManagementContext _context;
        private readonly IMapper _mapper;

        public ExpensesController(ExpenseManagementContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        [HttpGet("top-expenses/{userId}")]
        public IActionResult GetTopExpenseCategories(int userId)
        {
            var topExpenses = _context.Expenses
                .Where(e => e.UserId == userId)
                .GroupBy(e => e.CategoryId)
                .Select(g => new
                {
                    CategoryId = g.Key,
                    CategoryName = _context.ExpenseCategories
                        .Where(c => c.CategoryId == g.Key)
                        .Select(c => c.CategoryName)
                        .FirstOrDefault(),
                    TotalAmount = g.Sum(e => e.Amount) 
                })
                .OrderByDescending(e => e.TotalAmount) 
                .Take(5) 
                .ToList();
            return Ok(topExpenses);
        }

        [HttpGet("total-expense-current-month/{userId}")]
        public async Task<IActionResult> GetTotalExpenseCurrentMonth(int userId)
        {
            var currentMonth = DateTime.UtcNow.Month;
            var currentYear = DateTime.UtcNow.Year;

            var totalExpense = await _context.Expenses
                .Where(e => e.UserId == userId && e.Date.HasValue && e.Date.Value.Year == currentYear && e.Date.Value.Month == currentMonth)
                .SumAsync(e => (decimal?)e.Amount) ?? 0;

            return Ok(totalExpense.ToString());
        }

        [HttpGet("expense-history/{userId}/month/{month}/year/{year}")]
        public IActionResult GetExpenseHistory(int userId, int? month = null, int? year = null)
        {
            var currentDate = DateTime.UtcNow;
            int currentMonth = currentDate.Month;
            int currentYear = currentDate.Year;

            // Nếu không có giá trị month và year, lấy tháng/năm hiện tại
            month ??= currentMonth;
            year ??= currentYear;

            var expenseItems = _context.Expenses
                             .Where(e => e.UserId == userId && e.Date.HasValue && e.Date.Value.Year == year && e.Date.Value.Month == month)
                             .GroupBy(e => e.CategoryId)
                             .Select(g => new
                             {
                                 CategoryId = g.Key,
                                 CategoryName = _context.ExpenseCategories
                                     .Where(c => c.CategoryId == g.Key)
                                     .Select(c => c.CategoryName)
                                     .FirstOrDefault(),
                                 TotalAmount = g.Sum(e => e.Amount)
                             })
                             .ToList();

            return Ok(expenseItems);
        }

        [HttpGet("history-expense/{userId}/{categoryId}")]
        public IActionResult GetExpenseHistory(int userId, int categoryId)
        {
            var expenses = _context.Expenses
                .Where(e => e.UserId == userId && e.CategoryId == categoryId)
                .OrderByDescending(e => e.Date)
                .Select(e => new
                {
                    e.ExpenseId,
                    e.Date,
                    e.Amount,
                    Description = e.Description == null ? "Không có ghi chú" : e.Description
                }).ToList();
            if (!expenses.Any())
            {
                return NotFound(new { message = "Không có lịch sử chi tiêu." });
            }
            return Ok(expenses);
        }

        [HttpDelete("delete-expense/{userId}/{expenseId}")]
        public IActionResult DeleteExpense(int userId, int expenseId)
        {
            var expense =  _context.Expenses.FirstOrDefault(e => e.UserId == userId && e.ExpenseId == expenseId);
            if (expense == null)
            {
                return NotFound(new { message = "Không tìm thấy khoản chi tiêu." });
            }
            _context.Expenses.Remove(expense);
            _context.SaveChanges();
            return Ok();
        }

        [HttpGet("current-month-expense/{userId}")]
        public IActionResult GetCurrentMonthExpense(int userId)
        {
            var currentYear = DateTime.Now.Year;
            var currentMonth = DateTime.Now.Month;

            var totalExpense = _context.Expenses
                .Where(e => e.UserId == userId && e.Date.Value.Year == currentYear && e.Date.Value.Month == currentMonth)
                .Sum(e => e.Amount);

            return Ok(new
            {
                Month = currentMonth + "/" + currentYear,
                TotalExpense = totalExpense
            });
        }

        [HttpGet("list-expense-category")]
        public IActionResult GetListCategory()
        {
            var list = _context.ExpenseCategories.Where(x => x.StatusId == 1).Select(x => new ExpenseCategory
            {
                CategoryId = x.CategoryId,
                CategoryName = x.CategoryName,
            }).ToList();
            return Ok(list);
        }

        [HttpPost("add-expense/{userId}")]
        public IActionResult AddExpense(int userId, [FromBody] AddExpenseRequest expenseRequest)
        {
            if (expenseRequest == null)
            {
                return BadRequest("Dữ liệu chi tiêu không hợp lệ.");
            }

            var expense = new Expense
            {
                UserId = userId,
                CategoryId = expenseRequest.CategoryId,
                Amount = expenseRequest.Amount,
                Date = expenseRequest.Date ?? DateTime.UtcNow, 
                Description = expenseRequest.Description,
                CreatedAt = DateTime.UtcNow,
                StatusId = 1
            };
            _context.Expenses.Add(expense);
            _context.SaveChanges();

            return NoContent();
        }
    }
}

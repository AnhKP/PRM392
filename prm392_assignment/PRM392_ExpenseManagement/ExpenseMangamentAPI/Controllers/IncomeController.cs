using AutoMapper;
using ExpenseMangamentAPI.DTO.Users;
using ExpenseMangamentAPI.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Linq;

namespace ExpenseMangamentAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class IncomeController : ControllerBase
    {
        private readonly ExpenseManagementContext _context;
        private readonly IMapper _mapper;

        public IncomeController(ExpenseManagementContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        //tong thu nhap thang hien tai
        [HttpGet("total-income-current/{userId}")]
        public IActionResult GetTotalIncome(int userId)
        {
            var currentDate = DateTime.UtcNow;
            var currentMonth = currentDate.Month;
            var currentYear = currentDate.Year;

            var totalIncome = _context.Incomes
                .Where(i => i.UserId == userId && i.Month == currentMonth && i.Year == currentYear)
                .Sum(i => (decimal?)i.Amount) ?? 0;

            return Ok( new
            {
                Month = currentMonth + "/" + currentYear,
                TotalExpense = totalIncome.ToString("N0")
            });
        }
        [HttpGet("total-current-month/{userId}")]
        public IActionResult GetTotalIncomeCurrentMonth(int userId)
        {
            var currentMonth = DateTime.UtcNow.Month;
            var currentYear = DateTime.UtcNow.Year;

            var totalIncome =  _context.Incomes
                .Where(i => i.UserId == userId && i.Month == currentMonth && i.Year == currentYear)
                .Sum(i => (decimal?)i.Amount) ?? 0;

            return Ok(totalIncome.ToString());
        }

        [HttpGet("top-5-income/{userId}")]
        public IActionResult GetTopMonthsByIncome(int userId)
        {
            var currentYear = DateTime.UtcNow.Year;
            var topMonths = _context.Incomes
                .Where(i => i.UserId == userId && i.Year == currentYear)
                .GroupBy(i => i.Month) // Nhóm theo tháng
                .Select(g => new
                {
                    Month = g.Key,
                    TotalIncome = g.Sum(i => i.Amount) 
                })
                .OrderByDescending(g => g.TotalIncome) 
                .Take(5)
                .ToList();
            return Ok(topMonths);
        }

        //Lich su thu nhap
        [HttpGet("income-history/{userId}/month/{month}/year/{year}")]
        public IActionResult GetIncomeHistory(int userId, int? month = null, int? year = null)
        {
            var currentDate = DateTime.UtcNow;
            int currentMonth = currentDate.Month;
            int currentYear = currentDate.Year;

            // Nếu không có giá trị month và year, lấy tháng/năm hiện tại
            month ??= currentMonth;
            year ??= currentYear;

            var incomeHistory = _context.Incomes
                .Where(i => i.UserId == userId && i.Month == month && i.Year == year)
                .OrderByDescending(i => i.CreatedAt)
                .Select(i => new
                {
                    i.UserId,
                    i.IncomeId,
                    i.Amount,
                    i.Month,
                    i.Year,
                    Date = i.CreatedAt.Value.ToString("dd/MM/yyyy")
                })
                .ToList();

            return Ok(incomeHistory);
        }

        [HttpDelete("income-delete/{incomeid}")]
        public IActionResult DeleteIncome (int incomeid)
        {
            var income =  _context.Incomes.FirstOrDefault(x => x.IncomeId == incomeid);
            if (income == null)
            {
                return NotFound(new { message = "Khoản thu nhập không tồn tại!" });
            }
            _context.Incomes.Remove(income);
            _context.SaveChangesAsync();
            return NoContent();
        }

        [HttpPost("income/{userId}/add")]
        public IActionResult AddIncome(int userId, [FromBody] AddIncomeRequest incomeRequest)
        {
            if (incomeRequest == null)
            {
                return BadRequest("Invalid request data.");
            }

            var income = new Income
            {
                UserId = userId,
                Amount = incomeRequest.Amount,
                Year = incomeRequest.Year,
                Month = incomeRequest.Month,
                CreatedAt =  DateTime.UtcNow,
                StatusId = 1
            };
            _context.Incomes.Add(income);
            _context.SaveChanges();

            return Ok();
        }
    }
}

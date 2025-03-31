using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using ExpenseMangamentAPI.Models;

namespace ExpenseManagementWeb.Pages.CommonPage
{
    public class Admin_HomePageModel : PageModel
    {
        public List<Income> users { get; set; }
        public List<ExpenseCategory> Categories { get; set; }
        [BindProperty]
        public int totalUser { get; set; }
        [BindProperty]
        public int totalCate { get; set; }
        public void OnGet()
        {
            //
            totalUser = ExpenseManagementContext.instance.Users.Where(x => x.RoleId != 1).ToList().Count();
            totalCate = ExpenseManagementContext.instance.ExpenseCategories.ToList().Count();

            int currentMonth = DateTime.Now.Month;
            int currentYear = DateTime.Now.Year;

            var topUsersByTotalIncome = ExpenseManagementContext.instance.Incomes
                .Where(i => i.Month == currentMonth && i.Year == currentYear) // L?c theo tháng hi?n t?i
                .GroupBy(i => i.UserId)
                .Select(g => new
                {
                    UserId = g.Key,
                    TotalIncome = g.Sum(i => i.Amount)
                })
                .OrderByDescending(u => u.TotalIncome)
                .Take(5)
                .Join(ExpenseManagementContext.instance.Users,
                      income => income.UserId,
                      user => user.UserId,
                      (income, user) => new
                      {
                          user.FullName, // L?y tên ng??i dùng
                          income.TotalIncome
                      })
                .ToList();
            var topUsersByAverageIncome = ExpenseManagementContext.instance.Incomes
                                        .GroupBy(i => i.UserId)
                                        .Select(g => new
                                        {
                                            UserId = g.Key,
                                            AverageIncome = g.Average(i => i.Amount)
                                        })
                                        .OrderByDescending(u => u.AverageIncome)
                                        .Take(5)
                                        .Join(
                                            ExpenseManagementContext.instance.Users,       
                                            income => income.UserId,
                                            user => user.UserId,
                                            (income, user) => new
                                            {
                                                user.UserId,
                                                user.FullName,    
                                                income.AverageIncome
                                            }
                                        )
                                        .ToList();

            var topExpenseItems = ExpenseManagementContext.instance.Expenses
                                                .GroupBy(e => e.CategoryId)
                                                .Select(g => new
                                                {
                                                    ItemName = g.Key,
                                                    TotalSpent = g.Sum(e => e.Amount)
                                                })
                                                .OrderByDescending(e => e.TotalSpent)
                                                .Take(5)
                                                .ToList();         
        }
    }
}

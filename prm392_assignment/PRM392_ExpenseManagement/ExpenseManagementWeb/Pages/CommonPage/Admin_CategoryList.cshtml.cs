using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using ExpenseMangamentAPI.Models;

namespace ExpenseManagementWeb.Pages.CommonPage
{
    public class Admin_CategoryListModel : PageModel
    {
        public List<ExpenseCategory> Categories { get; set; }
        public void OnGet()
        {
            Categories = ExpenseManagementContext.instance.ExpenseCategories.Include(x => x.Status).ToList();
        }
    }
}

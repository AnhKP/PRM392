using ExpenseMangamentAPI.Models;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;

namespace ExpenseManagementWeb.Pages.CommonPage
{
    public class LoginModel : PageModel
    {
        ExpenseManagementContext context = new ExpenseManagementContext();
        public void OnGet()
        {
        }
        [HttpPost]
        public IActionResult OnPost()
        {
            string username = Request.Form["username"];
            string password = Request.Form["password"];
            var user = context.Users.FirstOrDefault(u =>
                        (u.Username.Equals(username) || u.Email.Equals(username)) &&
                        u.Password.Equals(password) && u.RoleId == 1);

                HttpContext.Session.SetString("Username", user.Username);
                HttpContext.Session.SetString("Fullname", user.FullName);
                HttpContext.Session.SetInt32("UserID", user.UserId);
                return RedirectToPage("/CommonPage/Admin_HomePage");
        }
    }
}

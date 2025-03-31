using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using Microsoft.EntityFrameworkCore;
using ExpenseMangamentAPI.Models;

namespace ExpenseManagementWeb.Pages.CommonPage
{
    public class Admin_UserListModel : PageModel
    {
        public List<User> Users { get; set; }

        public List<Role>   roles { get; set; }
        public void OnGet(string uid, string role)
        {
            if (uid != null && role != null)
            {
                int roleID = int.Parse(role);
                int UID = int.Parse(uid);
                var uObject = ExpenseManagementContext.instance.Users.FirstOrDefault(x => x.UserId == UID);
                if (uObject != null)
                {
                    uObject.RoleId = roleID;
                    ExpenseManagementContext.instance.Update(uObject);
                    ExpenseManagementContext.instance.SaveChanges();
                }
            }
            var user = ExpenseManagementContext.instance.Users.Include(x => x.Role).Include(x => x.Status).Where(x => x.RoleId != 1).ToList();
            Users = user;
            roles = ExpenseManagementContext.instance.Roles.Where(x => x.RoleId != 1).ToList();
        }
        public IActionResult OnGetDeleteUser(int userId)
        {
            var user = ExpenseManagementContext.instance.Users.Find(userId);
            if (user != null)
            {
                user.StatusId = 2;
                ExpenseManagementContext.instance.SaveChanges();
            }
            return RedirectToPage("/CommonPage/Admin_UserList");
        }
    }
}

using ExpenseMangamentAPI.Models;
using System.Text.RegularExpressions;

namespace ExpenseMangamentAPI.Validate
{
    public class InputValidate
    {
        public InputValidate()
        {

        }
        private readonly ExpenseManagementContext context = new ExpenseManagementContext();
        public bool isExistedEmail(string email)
        {
            var user = context.Users.FirstOrDefault(u => u.Email.Equals(email));
            if (user != null)
            {
                return true;
            }
            return false;
        }

        public bool ValidatePassword(string password)
        {
            string pattern = @"^(?=.*\d)(?=.*[\W_]).+$";
            if (string.IsNullOrEmpty(password) || !Regex.IsMatch(password, pattern))
            {
                return false;
            }
            return true;
        }

        public bool ValidateEmail(string email)
        {
            string pattern = @"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
            if (string.IsNullOrEmpty(email) || !Regex.IsMatch(email, pattern) || !isExistedEmail(email))
            {
                return false;
            }
            return true;
        }
    }
}

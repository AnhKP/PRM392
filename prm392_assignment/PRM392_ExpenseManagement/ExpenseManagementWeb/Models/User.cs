using System;
using System.Collections.Generic;

namespace ExpenseMangamentAPI.Models
{
    public partial class User
    {
        public User()
        {
            Expenses = new HashSet<Expense>();
            Incomes = new HashSet<Income>();
        }

        public int UserId { get; set; }
        public string? Username { get; set; } = null!;
        public string Password { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string? PhoneNumber { get; set; }
        public string? FullName { get; set; }
        public string? ProfilePicture { get; set; }
        public DateTime? CreatedAt { get; set; }
        public DateTime? UpdatedAt { get; set; }
        public int? RoleId { get; set; }
        public int? StatusId { get; set; }

        public virtual Role? Role { get; set; }
        public virtual Status? Status { get; set; }
        public virtual ICollection<Expense> Expenses { get; set; }
        public virtual ICollection<Income> Incomes { get; set; }
    }
}

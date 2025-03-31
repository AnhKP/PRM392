using System;
using System.Collections.Generic;

namespace ExpenseMangamentAPI.Models
{
    public partial class Status
    {
        public Status()
        {
            ExpenseCategories = new HashSet<ExpenseCategory>();
            Expenses = new HashSet<Expense>();
            Incomes = new HashSet<Income>();
            Users = new HashSet<User>();
        }

        public int StatusId { get; set; }
        public string StatusName { get; set; } = null!;

        public virtual ICollection<ExpenseCategory> ExpenseCategories { get; set; }
        public virtual ICollection<Expense> Expenses { get; set; }
        public virtual ICollection<Income> Incomes { get; set; }
        public virtual ICollection<User> Users { get; set; }
    }
}

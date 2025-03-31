using System;
using System.Collections.Generic;

namespace ExpenseMangamentAPI.Models
{
    public partial class ExpenseCategory
    {
        public ExpenseCategory()
        {
            Expenses = new HashSet<Expense>();
        }

        public int CategoryId { get; set; }
        public string? CategoryName { get; set; }
        public DateTime? CreatedAt { get; set; }
        public int? StatusId { get; set; }

        public virtual Status? Status { get; set; }
        public virtual ICollection<Expense> Expenses { get; set; }
    }
}

using System;
using System.Collections.Generic;

namespace ExpenseMangamentAPI.Models
{
    public partial class Expense
    {
        public int ExpenseId { get; set; }
        public int? UserId { get; set; }
        public int? CategoryId { get; set; }
        public decimal? Amount { get; set; }
        public string? Description { get; set; }
        public DateTime? Date { get; set; }
        public DateTime? CreatedAt { get; set; }
        public int? StatusId { get; set; }

        public virtual ExpenseCategory? Category { get; set; }
        public virtual Status? Status { get; set; }
        public virtual User? User { get; set; }
    }
}

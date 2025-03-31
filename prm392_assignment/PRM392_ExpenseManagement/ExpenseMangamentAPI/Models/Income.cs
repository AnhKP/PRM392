using System;
using System.Collections.Generic;

namespace ExpenseMangamentAPI.Models
{
    public partial class Income
    {
        public int IncomeId { get; set; }
        public int? UserId { get; set; }
        public decimal? Amount { get; set; }
        public int? Month { get; set; }
        public int? Year { get; set; }
        public DateTime? CreatedAt { get; set; }
        public int? StatusId { get; set; }

        public virtual Status? Status { get; set; }
        public virtual User? User { get; set; }
    }
}

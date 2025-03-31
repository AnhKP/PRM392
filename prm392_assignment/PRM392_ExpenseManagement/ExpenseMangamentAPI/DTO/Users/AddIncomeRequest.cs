namespace ExpenseMangamentAPI.DTO.Users
{
    public class AddIncomeRequest
    {
        public decimal? Amount { get; set; }
        public int? Month { get; set; }
        public int? Year { get; set; }

    }
}

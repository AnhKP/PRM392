namespace ExpenseMangamentAPI.DTO.Incomes
{
    public class GetIncomeByUserIdResponse
    {
        public int? UserId { get; set; }
        public decimal? Amount { get; set; }
        public int? Month { get; set; }
        public int? Year { get; set; }
    }
}

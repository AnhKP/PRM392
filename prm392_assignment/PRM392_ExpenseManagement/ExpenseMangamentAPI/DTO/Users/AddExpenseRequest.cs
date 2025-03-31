namespace ExpenseMangamentAPI.DTO.Users
{
    public class AddExpenseRequest
    {
        public int CategoryId { get; set; }
        public decimal Amount { get; set; }
        public DateTime? Date { get; set; } // Ngày có thể là null, mặc định là ngày hiện tại
        public string? Description { get; set; }
    }
}

namespace ExpenseMangamentAPI.DTO.Users
{
    public class UpdateProfileRequest
    {
        public int UserId { get; set; }
        public string Password { get; set; } = null!;
        public string? PhoneNumber { get; set; }
        public string? FullName { get; set; }
    }
}

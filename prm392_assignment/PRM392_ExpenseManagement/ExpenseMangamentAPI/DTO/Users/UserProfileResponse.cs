namespace ExpenseMangamentAPI.DTO.Users
{
    public class UserProfileResponse
    {
        public int UserId { get; set; }
        public string Username { get; set; } = null!;
        public string Password { get; set; } = null!;
        public string Email { get; set; } = null!;
        public string? PhoneNumber { get; set; }
        public string? FullName { get; set; }
        public string? Status { get; set; }
    }
}

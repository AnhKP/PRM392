using AutoMapper;
using ExpenseMangamentAPI.DTO.Users;
using ExpenseMangamentAPI.Models;
using ExpenseMangamentAPI.Validate;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using System.Text.RegularExpressions;

namespace ExpenseMangamentAPI.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly ExpenseManagementContext _context;
        private readonly IMapper _mapper;

        public UserController(ExpenseManagementContext context, IMapper mapper)
        {
            _context = context;
            _mapper = mapper;
        }

        [HttpPost("CheckLoginUser")]
        public ActionResult<User> checkLoginUser([FromBody] LoginUserRequest request)
        {
            if (string.IsNullOrEmpty(request.Email) || string.IsNullOrEmpty(request.Password))
            {
                return BadRequest("Email và mật khẩu không được để trống.");
            }
            var user = _context.Users.FirstOrDefault(u => u.Email.Equals(request.Email) && u.Password.Equals(request.Password) && u.RoleId == 2 && u.StatusId == 1);

            if (user == null)
            {
                return BadRequest("Email or password is not correct!");
            }
            return Ok(user);
        }

        [HttpPost("RegisterAccount")]
        public ActionResult<User> registerAccount([FromBody] RegisterUserRequest request)
        {
            var user = _context.Users.FirstOrDefault(u => u.Email.Equals(request.Email));
            if (user != null)
            {
                return BadRequest("Email is existed!");
            }
            var users = _mapper.Map<User>(request);
            users.Username = request.FullName;
            _context.Users.Add(users);
            _context.SaveChanges();
            return Ok(users);
        }

        [HttpGet("user-profile/{id}")]

        public IActionResult GetUserProfile(int id)
        {
            var user = _context.Users.Include(x => x.Status).FirstOrDefault(u => u.UserId == id);
            if (user == null)
            {
                return NotFound("User not found");
            }
            return Ok(_mapper.Map<UserProfileResponse>(user));
        }

        [HttpGet("emails")]
        public ActionResult<List<string>> GetRegisteredEmails()
        {
            var emails = _context.Users.Select(u => u.Email).ToList();
            return Ok(emails);
        }

        [HttpPut("update-profile/{userId}")]
        public IActionResult UpdateProfile(int userId, [FromBody] UpdateProfileRequest model)
        {
            var user =  _context.Users.Find(userId);
            if (user == null)
            {
                return NotFound("Người dùng không tồn tại");
            }
            user.FullName = !string.IsNullOrEmpty(model.FullName) ? model.FullName : user.FullName;
            user.PhoneNumber = !string.IsNullOrEmpty(model.PhoneNumber) ? model.PhoneNumber : user.PhoneNumber;

            if (!string.IsNullOrEmpty(model.Password))
            {
                user.Password = model.Password;
            }

            _context.Users.Update(user);
             _context.SaveChangesAsync();

            return Ok(user);
        }

    }
}

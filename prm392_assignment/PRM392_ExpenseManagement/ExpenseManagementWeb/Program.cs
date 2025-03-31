using ExpenseMangamentAPI.Models;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
// Thêm Razor Pages và các dịch vụ khác
builder.Services.AddRazorPages();


// Thêm dịch vụ cho Session
builder.Services.AddDistributedMemoryCache();
builder.Services.AddSession(options =>
{
    options.IdleTimeout = TimeSpan.FromMinutes(30); // Thời gian hết hạn của session (30 phút)
    options.Cookie.HttpOnly = true; // Chỉ cho phép session qua HTTP, không qua JavaScript
    options.Cookie.IsEssential = true; // Đảm bảo session được sử dụng ngay cả khi cookie là không cần thiết
});
builder.Services.AddDbContext<ExpenseManagementContext>(
    option => option.UseSqlServer(builder.Configuration.GetConnectionString("MyCnn")));

var app = builder.Build();

// Configure the HTTP request pipeline.
if (!app.Environment.IsDevelopment())
{
    // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
    app.UseHsts();
}
app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

// Thêm middleware cho session
app.UseSession();

app.UseAuthorization();

app.MapRazorPages();

// Đặt trang mặc định khi truy cập "/"
app.MapGet("/", context =>
{
    context.Response.Redirect("/CommonPage/Login");
    return Task.CompletedTask;
});

app.Run();


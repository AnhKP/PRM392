using AutoMapper;
using ExpenseMangamentAPI.DTO.Incomes;
using ExpenseMangamentAPI.DTO.Users;
using ExpenseMangamentAPI.Models;

namespace ExpenseMangamentAPI.DTO
{
    public class MappedConfig : Profile
    {
        public MappedConfig()
        {
            CreateMap<User, UserProfileResponse>().ForMember(dest => dest.Status, opt => opt.MapFrom(src => src.Status.StatusName)).ReverseMap();
            CreateMap<User, LoginUserRequest>().ReverseMap();
            CreateMap<RegisterUserRequest, User>().ForMember(dest => dest.StatusId, opt => opt.MapFrom(src => 1))
                                                  .ForMember(dest => dest.CreatedAt, opt => opt.MapFrom(src => DateTime.UtcNow))
                                                  .ForMember(dest => dest.RoleId, opt => opt.MapFrom(src => 2)).ReverseMap(); ;
            CreateMap<UpdateProfileRequest, User>().ForMember(dest => dest.UpdatedAt, opt => opt.MapFrom(src => DateTime.UtcNow));
            CreateMap<AddExpenseRequest, Expense>().ForMember(dest => dest.CreatedAt, opt => opt.MapFrom(src => DateTime.UtcNow))
                                                    .ForMember(dest => dest.StatusId, opt => opt.MapFrom(src => 1)).ReverseMap(); ;

            CreateMap<AddIncomeRequest, Income>().ReverseMap(); ;
        }
    }
}

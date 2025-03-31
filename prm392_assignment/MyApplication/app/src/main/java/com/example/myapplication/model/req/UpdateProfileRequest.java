package com.example.myapplication.model.req;

public class UpdateProfileRequest {

        private int userId;
        private String password;
        private String phoneNumber;
        private String fullName;

        public UpdateProfileRequest() {

        }

        public UpdateProfileRequest(int userId, String password, String phoneNumber, String fullName) {
            this.userId = userId;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.fullName = fullName;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
}

package com.example.comicapp.dto.response;

public class RegisterResponse {

        private boolean success;
        private String message;
        private UserData data;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public UserData getData() {
            return data;
        }

        public void setData(UserData data) {
            this.data = data;
        }

        public static class UserData {
            private Long id;
            private String username;
            private String email;

            // Getters and Setters
            public Long getId() {
                return id;
            }

            public void setId(Long id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }

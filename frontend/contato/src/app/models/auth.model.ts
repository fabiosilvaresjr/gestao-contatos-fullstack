export interface LoginRequest {
  login: string;
  password?: string;
  role?: string;
}

export interface LoginResponse {
  token: string;
}

package nz.ac.aut.comp713.library.api;

public class LoginResponse {

    private Long memberId;
    private String name;
    private String email;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(
            Long memberId,
            String name,
            String email,
            String token) {

        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
package nz.ac.aut.comp713.library.api;

import nz.ac.aut.comp713.library.domain.Member;
import nz.ac.aut.comp713.library.service.MemberService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;

@Path("/members")
@Produces("application/json")
public class MemberResource {

    @Inject
    private MemberService memberService;

    @GET
    public List<Member> getMembers() {
        return memberService.getMembers();
    }
}
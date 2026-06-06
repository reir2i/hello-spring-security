package kr.ac.hansung.controller;

import kr.ac.hansung.dto.PasswordChangeDto;
import kr.ac.hansung.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/password")
    public String passwordForm(@ModelAttribute PasswordChangeDto dto) {
        return "user/password";
    }

    @PostMapping("/user/password")
    public String changePassword(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute PasswordChangeDto dto,
            RedirectAttributes ra) {

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            ra.addFlashAttribute("errorMessage", "새 비밀번호가 일치하지 않습니다");
            return "redirect:/user/password";
        }

        try {
            userService.changePassword(userDetails.getUsername(),
                    dto.getCurrentPassword(), dto.getNewPassword());
            ra.addFlashAttribute("successMessage", "비밀번호가 변경되었습니다");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/password";
        }

        return "redirect:/home";
    }
}
package org.smartshop.smartshop.utils;

import jakarta.servlet.http.HttpSession;
import org.smartshop.smartshop.DTO.auth.LoginResponseDTO;
import org.smartshop.smartshop.enums.UserRole;
import org.smartshop.smartshop.exception.ForbiddenException;
import org.smartshop.smartshop.exception.UnauthorizedException;

public class SecurityAuth {

    public static LoginResponseDTO getAuthenticatedUser(HttpSession session) {
        LoginResponseDTO user = (LoginResponseDTO) session.getAttribute("user");
        if (user == null) {
            throw new UnauthorizedException("Non authentifie");
        }
        return user;
    }

    public static Long getAuthenticatedUserId(HttpSession session) {
        LoginResponseDTO user = getAuthenticatedUser(session);
        return user.getUserId();
    }

    public static void requireAdmin(HttpSession session) {
        LoginResponseDTO user = getAuthenticatedUser(session);

        if (user.getRole() != UserRole.ADMIN) {
            throw new ForbiddenException("Acces reserve aux administrateurs");
        }
    }

    public static void requireClient(HttpSession session) {
        LoginResponseDTO user = getAuthenticatedUser(session);

        if (user.getRole() != UserRole.CLIENT) {
            throw new ForbiddenException("Acces reserve aux clients");
        }
    }


}
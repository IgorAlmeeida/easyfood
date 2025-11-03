package br.com.imsa.easyfood.domain.service;

public interface UserSystemPasswordService {

    void changePassword(Long id, String oldPassword, String newPassword);

}

package br.com.imsa.easyfood.api.dto.responses;

import br.com.imsa.easyfood.domain.enums.UserTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSystemResponse {

    private Long id;

    private String username;

    private String email;

    private String name;

    private AddressResponse address;

    private String userType;

    void setUserType(UserTypeEnum userTypeEnum) {
        this.userType = userTypeEnum.getAcronym();
    }



}

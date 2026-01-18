package br.com.imsa.easyfood.infra.adpter;

import br.com.imsa.easyfood.domain.entity.Address;
import br.com.imsa.easyfood.domain.gateway.AddressGateway;
import br.com.imsa.easyfood.infra.mappers.AddressMapper;
import br.com.imsa.easyfood.infra.model.AddressJpaEntity;
import br.com.imsa.easyfood.infra.repository.AddressRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddressEntityRepository implements AddressGateway {

    private final AddressRepository repository;
    private final AddressMapper mapper;


    @Override
    public Address save(Address address) {
        AddressJpaEntity jpaEntity = mapper.toEntity(address);
        return repository.save(jpaEntity);
    }

    @Override
    public Address update(Long id, Address address) {
        AddressJpaEntity jpaEntity = mapper.toEntity(address);
        jpaEntity.setId(id);
        return repository.save(jpaEntity);
    }
}

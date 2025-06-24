package br.edu.atitus.api_sample.services;

import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import br.edu.atitus.api_sample.entities.PointEntity;
import br.edu.atitus.api_sample.entities.UserEntity;
import br.edu.atitus.api_sample.repositories.PointRepository;
import jakarta.transaction.Transactional;

@Service
public class PointService {

	private final PointRepository repository;

	public PointService(PointRepository repository) {
		super();
		this.repository = repository;
	}
	
	@Transactional
	public PointEntity save(PointEntity point) throws Exception {
		if (point == null)
			throw new Exception("Objeto nulo");
		
		if (point.getDescription() == null || point.getDescription().isEmpty())
			throw new Exception("Descrição inválida");
		if (point.getLatitude() < -90 || point.getLatitude() > 90)
			throw new Exception("Latitude inválida");
		if (point.getLongitude() < -180 || point.getLongitude() > 180)
			throw new Exception("Longitude inválida");
		
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		point.setUser(userAuth);

		return repository.save(point);
	}
	
	@Transactional
	public void deleteById(UUID id) throws Exception {
		var pointInBD = repository.findById(id).orElseThrow(() -> new Exception("Ponto não localizado"));
		UserEntity userAuth = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!pointInBD.getUser().getId().equals(userAuth.getId()))
			throw new Exception("Você não tem permissão para essa ação");
		
		repository.deleteById(id);
	}
	
	public List<PointEntity> findAll() {
		return repository.findAll();
	}
	
	
	
}
//
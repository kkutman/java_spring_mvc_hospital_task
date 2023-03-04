package peaksoft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import peaksoft.entity.Hospital;
import peaksoft.exeptions.NotFoundException;
import peaksoft.repositories.HospitalRepository;
import peaksoft.service.HospitalService;

import java.util.List;

/**
 * @author kurstan
 * @created at 17.02.2023 12:45
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalServiceImpl(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
    }

    @Override
    public List<Hospital> getAllHospitals() {
        return hospitalRepository.findAll();
    }

    @Override
    public void save(Hospital hospital) {
        hospitalRepository.save(hospital);
    }

    @Override
    public void delete(Long id) {
        hospitalRepository.deleteById(id);
    }

    @Override
    public Hospital getById(Long id) {
        return hospitalRepository
                .findById(id).orElseThrow(
                        ()-> new NotFoundException("Hospital by id " + id + " not found"));
    }

    @Override
    public void update(Long id, Hospital hospital) {
        Hospital oldHospital = hospitalRepository
                .findById(id).orElseThrow(
                        () -> new NotFoundException("Hospital by id " + id + " not found"));
        oldHospital.setName(hospital.getName());
        oldHospital.setAddress(hospital.getAddress());
        oldHospital.setImage(hospital.getImage());
        hospitalRepository.save(oldHospital);
    }

    @Override
    public List<Hospital> getAllHospitals(String keyWord) {
        if (keyWord != null && !keyWord.trim().isEmpty()){
           return hospitalRepository.search("%" + keyWord + "%");
        } else {
            return hospitalRepository.findAll();
        }
    }
}

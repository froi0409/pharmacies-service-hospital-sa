package com.sa.pharmacies.pharmacy.application.createdrug;

import com.sa.pharmacies.common.annotation.UseCase;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.CreateDrugInputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.FindDrugByNameOutputPort;
import com.sa.pharmacies.drug.infrastructure.outputports.db.SaveDrugOutputPort;
import jakarta.transaction.Transactional;

import java.util.Optional;

@Transactional
@UseCase
public class CreateDrugUseCase implements CreateDrugInputPort {
    private final FindDrugByNameOutputPort findDrugByNameOutputPort;
    private final SaveDrugOutputPort saveDrugOutputPort;

    public CreateDrugUseCase(FindDrugByNameOutputPort findDrugByNameOutputPort, SaveDrugOutputPort saveDrugOutputPort) {
        this.findDrugByNameOutputPort = findDrugByNameOutputPort;
        this.saveDrugOutputPort = saveDrugOutputPort;
    }

    @Override
    public String createDrug(CreateDrugRequest request) throws IllegalArgumentException, EntityAlreadyExistsException {
        Drug drug = request.toDomain();

        //validate
        if (!drug.validateCost()){
            throw new IllegalArgumentException("the cost is not valid");
        }

        if (!drug.validateMinimumQuantity()){
            throw new IllegalArgumentException("the quantity is not valid");
        }

        if (!drug.validateUnitPrice()){
            throw new IllegalArgumentException("the price is not valid");
        }

        Optional<Drug> isExistDrug = findDrugByNameOutputPort.findByName(drug.getName());
        if (isExistDrug.isPresent()){
            throw new EntityAlreadyExistsException("the name already exists");
        }

        //save into the database
        drug = saveDrugOutputPort.save(drug);

        //return the code for whatever they wanted
        return  drug.getCode().toString();
    }
}

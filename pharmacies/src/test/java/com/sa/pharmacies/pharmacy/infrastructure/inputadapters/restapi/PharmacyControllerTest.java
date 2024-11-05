package com.sa.pharmacies.pharmacy.infrastructure.inputadapters.restapi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sa.pharmacies.common.exceptions.EntityAlreadyExistsException;
import com.sa.pharmacies.drug.domain.Drug;
import com.sa.pharmacies.pharmacy.application.assignpharmacydrug.AssignPharmacyDrugRequest;
import com.sa.pharmacies.pharmacy.application.createdrug.CreateDrugRequest;
import com.sa.pharmacies.pharmacy.application.getalllistdrugdatabypharmacy.GetAllListDrugDataByPharmacyResponse;
import com.sa.pharmacies.pharmacy.application.getdrugbycode.GetDrugByCodeResponse;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetItemDrugDataByCodesResponse;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabycodes.GetListDrugDataByCodesRequest;
import com.sa.pharmacies.pharmacy.application.getlistdrugdatabynameslike.GetItemDrugDataByNamesLikeResponse;
import com.sa.pharmacies.pharmacy.domain.Pharmacy;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.CreatePharmacyByEventInputPort;
import com.sa.pharmacies.pharmacy.infrastructure.inputports.restapi.ExistsPharmacyInputPort;
import com.sa.pharmacies.pharmacydrug.infrastructure.inputports.restapi.AssignPharmacyDrugInputPort;
import com.sa.pharmacies.drug.infrastructure.inputports.restapi.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class PharmacyControllerTest {

    @Mock
    private CreatePharmacyByEventInputPort createPharmacyByEventInputPort;

    @Mock
    private CreateDrugInputPort createDrugInputPort;

    @Mock
    private GetListDrugDataByCodesInputPort getListDrugDataByCodesInputPort;

    @Mock
    private AssignPharmacyDrugInputPort assignPharmacyDrugInputPort;

    @Mock
    private ExistsPharmacyInputPort existsPharmacyInputPort;

    @Mock
    private GetListDrugDataByNamesLikeInputPort getListDrugDataByNamesLikeInputPort;

    @Mock
    private ExistsDrugInputPort existsDrugInputPort;

    @Mock
    private GetDrugByCodeInputPort getDrugByCodeInputPort;

    @Mock
    private GetAllListDrugDataByPharmacyInputPort getAllListDrugDataByPharmacyInputPort;

    @InjectMocks
    private PharmacyController pharmacyController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePharmacy() throws EntityAlreadyExistsException, EntityAlreadyExistsException {
        String idArea = "Pharmacy-123";

        ResponseEntity<String> response = pharmacyController.createPharmacy(idArea);

        verify(createPharmacyByEventInputPort, times(1)).createPharmacyByEvent(idArea);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Pharmacy created", response.getBody());
    }

    @Test
    public void testCreateDrug() throws EntityAlreadyExistsException {
        CreateDrugRequest createDrugRequest = new CreateDrugRequest("Ibuprofen", 10.0, 50, 12.5);
        String expectedId = "Drug-123";

        when(createDrugInputPort.createDrug(createDrugRequest)).thenReturn(expectedId);

        ResponseEntity<String> response = pharmacyController.createDrug(createDrugRequest);

        verify(createDrugInputPort, times(1)).createDrug(createDrugRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedId, response.getBody());
    }

    @Test
    public void testGetListDrugDataByNamesLike() {
        String name = "Ibu";
        List<GetItemDrugDataByNamesLikeResponse> expectedResponse = List.of(
                new GetItemDrugDataByNamesLikeResponse("Drug-1", "Ibuprofen")
        );

        when(getListDrugDataByNamesLikeInputPort.getDrugNamesLike(name)).thenReturn(expectedResponse);

        ResponseEntity<List<GetItemDrugDataByNamesLikeResponse>> response = pharmacyController.getListDrugDataByNamesLike(name);

        verify(getListDrugDataByNamesLikeInputPort, times(1)).getDrugNamesLike(name);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testGetListDrugDataByCodes() {
        GetListDrugDataByCodesRequest request = new GetListDrugDataByCodesRequest(List.of("Drug-1"));
        List<GetItemDrugDataByCodesResponse> expectedResponse = List.of(
                new GetItemDrugDataByCodesResponse("Drug-1", "Ibuprofen")
        );

        when(getListDrugDataByCodesInputPort.getList(request)).thenReturn(expectedResponse);

        ResponseEntity<List<GetItemDrugDataByCodesResponse>> response = pharmacyController.getListDrugDataByCodes(request);

        verify(getListDrugDataByCodesInputPort, times(1)).getList(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testAssignPharmacyDrug() throws EntityAlreadyExistsException, JsonProcessingException {
        AssignPharmacyDrugRequest request = new AssignPharmacyDrugRequest(10, "Pharmacy-123", "Drug-456");

        ResponseEntity<String> response = pharmacyController.assignPharmacyDrugInputPort(request);

        verify(assignPharmacyDrugInputPort, times(1)).assign(request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Drug assign to this pharmacy", response.getBody());
    }

    @Test
    public void testCheckPharmacyExists() {
        String id = "Pharmacy-123";

        when(existsPharmacyInputPort.getPharmacyById(id)).thenReturn(Optional.<Pharmacy>of(Pharmacy.builder().build()));

        ResponseEntity<Void> response = pharmacyController.checkPharmacyExists(id);

        verify(existsPharmacyInputPort, times(1)).getPharmacyById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testCheckDrugExists() {
        String code = "Drug-123";

        when(existsDrugInputPort.findByCode(code)).thenReturn(Optional.<Drug>of(Drug.builder().build()));

        ResponseEntity<Void> response = pharmacyController.checkDrugExists(code);

        verify(existsDrugInputPort, times(1)).findByCode(code);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetDrugData() {
        String code = "Drug-123";
        GetDrugByCodeResponse expectedResponse = new GetDrugByCodeResponse("Drug-123", "Ibuprofen", 10.0, 12.5, 50);

        when(getDrugByCodeInputPort.getDrugByCode(code)).thenReturn(expectedResponse);

        ResponseEntity<GetDrugByCodeResponse> response = pharmacyController.getDrugData(code);

        verify(getDrugByCodeInputPort, times(1)).getDrugByCode(code);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }

    @Test
    public void testGetAllDrugs() {
        List<GetAllListDrugDataByPharmacyResponse> expectedResponse = List.of(
                new GetAllListDrugDataByPharmacyResponse("Drug-1", "Ibuprofen", 10.0, 12.5, 100)
        );

        when(getAllListDrugDataByPharmacyInputPort.getAll()).thenReturn(expectedResponse);

        ResponseEntity<List<GetAllListDrugDataByPharmacyResponse>> response = pharmacyController.getAllDrugs();

        verify(getAllListDrugDataByPharmacyInputPort, times(1)).getAll();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    }
}

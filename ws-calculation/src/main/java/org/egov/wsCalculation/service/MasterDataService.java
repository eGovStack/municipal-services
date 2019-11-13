package org.egov.wsCalculation.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.egov.common.contract.request.RequestInfo;
import org.egov.mdms.model.MdmsCriteriaReq;

import org.egov.tracer.model.CustomException;
import org.egov.waterConnection.config.WSConfiguration;
import org.egov.waterConnection.repository.ServiceRequestRepository;
import org.egov.wsCalculation.constants.WSCalculationConstant;
import org.egov.wsCalculation.model.CalculationReq;
import org.egov.wsCalculation.model.RequestInfoWrapper;
import org.egov.wsCalculation.model.TaxHeadMaster;
import org.egov.wsCalculation.model.TaxHeadMasterResponse;
import org.egov.wsCalculation.model.TaxPeriod;
import org.egov.wsCalculation.model.TaxPeriodResponse;
import org.egov.wsCalculation.util.WSCalculationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@Service
public class MasterDataService {

	@Autowired
	private ServiceRequestRepository repository;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private WSCalculationUtil wSCalculationUtil;

	@Autowired
	private WSConfiguration config;

	/**
	 * Fetches and creates map of all required masters
	 * 
	 * @param request
	 *            The calculation request
	 * @return
	 */
	public Map<String, Object> getMasterMap(CalculationReq request) {
		RequestInfo requestInfo = request.getRequestInfo();
		String tenantId = request.getCalculationCriteria().get(0).getTenantId();
		Map<String, Object> masterMap = new HashMap<>();
		List<TaxPeriod> taxPeriods = getTaxPeriodList(requestInfo, tenantId);
		List<TaxHeadMaster> taxHeadMasters = getTaxHeadMasterMap(requestInfo, tenantId);
		//Map<String, Map<String, Object>> financialYearMaster = getFinancialYear(request);

		masterMap.put(WSCalculationConstant.TAXPERIOD_MASTER_KEY, taxPeriods);
		masterMap.put(WSCalculationConstant.TAXHEADMASTER_MASTER_KEY, taxHeadMasters);
		//masterMap.put(WSCalculationConstant.FINANCIALYEAR_MASTER_KEY, financialYearMaster);

		return masterMap;
	}

	/**
	 * Fetch Tax Head Masters From billing service
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @return
	 */
	public List<TaxPeriod> getTaxPeriodList(RequestInfo requestInfo, String tenantId) {

		StringBuilder uri = wSCalculationUtil.getTaxPeriodSearchUrl(tenantId);
		TaxPeriodResponse res = mapper.convertValue(
				repository.fetchResult(uri, RequestInfoWrapper.builder().requestInfo(requestInfo).build()),
				TaxPeriodResponse.class);
		return res.getTaxPeriods();
	}

	/**
	 * Fetch Tax Head Masters From billing service
	 * 
	 * @param requestInfo
	 * @param tenantId
	 * @return
	 */
	public List<TaxHeadMaster> getTaxHeadMasterMap(RequestInfo requestInfo, String tenantId) {

		StringBuilder uri = wSCalculationUtil.getTaxHeadSearchUrl(tenantId);
		TaxHeadMasterResponse res = mapper.convertValue(
				repository.fetchResult(uri, RequestInfoWrapper.builder().requestInfo(requestInfo).build()),
				TaxHeadMasterResponse.class);
		return res.getTaxHeadMasters();
	}

	/**
	 * Fetches Financial Year from Mdms Api
	 *
	 * @param req
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public Map<String, Map<String, Object>> getFinancialYear(CalculationReq req) {
//		String tenantId = req.getCalculationCriteria().get(0).getTenantId();
//		RequestInfo requestInfo = req.getRequestInfo();
//		Set<String> assessmentYears = req.getCalculationCriteria().stream()
//				.map(cal -> cal.getWaterConnection().getProperty().getProperPropertyDetails().get(0).getFinancialYear())
//				.collect(Collectors.toSet());
//		MdmsCriteriaReq mdmsCriteriaReq = wSCalculationUtil.getFinancialYearRequest(requestInfo, assessmentYears,
//				tenantId);
//		StringBuilder url = wSCalculationUtil.getMdmsSearchUrl();
//		Object res = repository.fetchResult(url, mdmsCriteriaReq);
//		Map<String, Map<String, Object>> financialYearMap = new HashMap<>();
//		for (String assessmentYear : assessmentYears) {
//			String jsonPath = MDMS_FINACIALYEAR_PATH.replace("{}", assessmentYear);
//			try {
//				List<Map<String, Object>> jsonOutput = JsonPath.read(res, jsonPath);
//				Map<String, Object> financialYearProperties = jsonOutput.get(0);
//				financialYearMap.put(assessmentYear, financialYearProperties);
//			} catch (IndexOutOfBoundsException e) {
//				throw new CustomException(CalculatorConstants.EG_PT_FINANCIAL_MASTER_NOT_FOUND,
//						CalculatorConstants.EG_PT_FINANCIAL_MASTER_NOT_FOUND_MSG + assessmentYear);
//			}
//		}
//		return financialYearMap;
//	}

}
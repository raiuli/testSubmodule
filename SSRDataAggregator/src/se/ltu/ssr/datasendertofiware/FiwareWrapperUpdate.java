package se.ltu.ssr.datasendertofiware;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import eu.neclab.iotplatform.ngsi.api.datamodel.ContextAttribute;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextElement;
import eu.neclab.iotplatform.ngsi.api.datamodel.ContextMetadata;
import eu.neclab.iotplatform.ngsi.api.datamodel.EntityId;
import eu.neclab.iotplatform.ngsi.api.datamodel.UpdateActionType;
import eu.neclab.iotplatform.ngsi.api.datamodel.UpdateContextRequest;
import se.ltu.ssr.adapter.interfaces.DataPacket;
import se.ltu.ssr.common.data.Record;
import se.ltu.ssr.common.data.SSRData;

public class FiwareWrapperUpdate {
	Logger log=Logger.getLogger(FiwareWrapperUpdate.class);

	public String createUpdaterequest(DataPacket dp){
		SSRData ssrData =(SSRData) dp;
		
	
		// creating entity Id
		EntityId entityId = new EntityId();
		entityId.setId(ssrData.getSourceID());
		entityId.setIsPattern(false);
		URI type = null;
		try {
			type = new  URI("sensor");
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
		entityId.setType(type);
		List<ContextAttribute> crAttributelst = new ArrayList<>();
		for(int i=0;i<ssrData.getRecords().size();i++){
			Record record=ssrData.getRecords().get(i);
			ContextAttribute ca = new ContextAttribute();
			ca.setName(record.getName());
			if(Float.parseFloat(record.getValue())<0){
				record.setValue("0");
			}
			ca.setContextValue(record.getValue());
			URI attrType = null;
			try {
				attrType = new  URI("string");
			} catch (URISyntaxException e) {
				
				e.printStackTrace();
			}
			ca.setType(attrType);
			// creating Metadata 
			List<ContextMetadata> cmdLst = new ArrayList<>();
			
			ContextMetadata cmd = new ContextMetadata();
			cmd.setName("unit");
			cmd.setValue(record.getUnit());
			try {
				type = new  URI("float");
			} catch (URISyntaxException e) {
				
				e.printStackTrace();
			}
			cmd.setType(type);
			cmdLst.add(cmd);
			
			ContextMetadata cmd_2 = new ContextMetadata();
			cmd_2.setName("description");
			cmd_2.setValue(record.getUnit());
			try {
				type = new  URI("string");
			} catch (URISyntaxException e) {
				
				e.printStackTrace();
			}
			cmd_2.setType(type);
			cmdLst.add(cmd_2);
			// adding Metadata to Attribute
			ca.setMetadata(cmdLst);
			
			crAttributelst.add(ca);
		}
		/*// creating attribute 
		ContextAttribute ca = new ContextAttribute();
		ca.setName(attributeName);
		ca.setContextValue(value);
		URI attrType = null;
		try {
			attrType = new  URI("float");
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
		ca.setType(attrType);
		// creating Metadata 
		List<ContextMetadata> cmdLst = new ArrayList<>();
		ContextMetadata cmd = new ContextMetadata();
		cmd.setName("unit");
		cmd.setValue("ppm");
		try {
			type = new  URI("string");
		} catch (URISyntaxException e) {
			
			e.printStackTrace();
		}
		cmd.setType(type);
		cmdLst.add(cmd);
		// adding Metadata to Attribute
		ca.setMetadata(cmdLst);
		
		crAttributelst.add(ca);*/
		// crating context Element
		 ContextElement ce = new ContextElement();
		 ce.setEntityId(entityId);
		 ce.setContextAttributeList(crAttributelst);
		 List<ContextElement> ceLst = new ArrayList<>();
		 ceLst.add(ce);
		 UpdateContextRequest ucr = new UpdateContextRequest();
		 ucr.setContextElement(ceLst);
		 ucr.setUpdateAction(UpdateActionType.APPEND);
		 
		return ucr.toJsonString();
		 //return ucr.toString();
		
	}
}

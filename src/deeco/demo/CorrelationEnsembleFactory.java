package deeco.demo;

import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import cz.cuni.mff.d3s.deeco.annotations.Ensemble;
import cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling;

@Ensemble
@PeriodicScheduling(period = 1000)
public class CorrelationEnsembleFactory {
	
	/**
	 * Once a new class is created it is stored here ready for further retrieval.
	 */
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> bufferedClasses = new HashMap<>();
	
	private static final long schedulingPeriod = 1000;
	
	/**
	 * 
	 * @param correlationFilter Name of the knowledge field used for data filtering when calculating correlation.
	 * 		In our example this refers to "position".
	 * @param correlationSubject Name of the knowledge field used for the calculation of data correlation after
	 * 		the values has been filtered. In our example this refers to "temperature".
	 * @return A class that defines an ensemble for data exchange given by the specified knowledge fields.
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Class getEnsembleDefinition(String correlationFilter, String correlationSubject) throws Exception {
		String className = composeClassName(correlationFilter, correlationSubject);
		Class requestedClass;
		if(!bufferedClasses.containsKey(className)){
			requestedClass = createEnsembleDefinition(correlationFilter, correlationSubject);
			bufferedClasses.put(className, requestedClass);
		}
		else {
			requestedClass = bufferedClasses.get(className);
		}
		
		return requestedClass;
	}
	
	private static String composeClassName(String correlationFilter, String correlationSubject){
		return String.format("Correlation_%s2%s", correlationFilter, correlationSubject);
	}
	
	@SuppressWarnings("rawtypes")
	private static Class createEnsembleDefinition(String correlationFilter, String correlationSubject) throws Exception {
		
		// Create the class defining the ensemble
		ClassPool classPool = ClassPool.getDefault();
		CtClass ensembleClass = classPool.makeClass(composeClassName(correlationFilter, correlationSubject));
		
		ClassFile classFile = ensembleClass.getClassFile();
		ConstPool constPool = classFile.getConstPool();
		
		// Ensemble annotation for the class
		Annotation ensembleAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.Ensemble", constPool);
		AnnotationsAttribute ensembleAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		ensembleAttribute.addAnnotation(ensembleAnnotation);
		// Scheduling annotation for the class
		Annotation schedulingAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.PeriodicScheduling", constPool);
		schedulingAnnotation.addMemberValue("period", new LongMemberValue(schedulingPeriod, constPool));
		ensembleAttribute.addAnnotation(schedulingAnnotation);
		// Add the class annotations
		classFile.addAttribute(ensembleAttribute);
		
		// Membership method
		CtMethod membershipMethod = CtNewMethod.make(String.format(
				"public static boolean membership("
				+ "		deeco.metadata.MetadataWrapper member%1$s,"
				+ "		deeco.metadata.MetadataWrapper member%2$s,"
				+ "		deeco.metadata.MetadataWrapper coord%1$s,"
				+ "		deeco.metadata.MetadataWrapper coord%2$s) {"
				+ " return (!member%2$s.isOperational()"
				+ "		&& coord%2$s.isOperational()"
				+ "		&& deeco.metadata.KnowledgeMetadataHolder.classifyDistance("
				+ "			\"%1$s\", member%1$s.getValue(), coord%1$s.getValue())"
				+ "			 == deeco.metadata.CorrelationLevel.DistanceClass.Close);}",
					correlationFilter, correlationSubject),
				ensembleClass);
		// Membership annotation for the membership method
		Annotation membershipAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.Membership", constPool);
		AnnotationsAttribute membershipAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		membershipAttribute.addAnnotation(membershipAnnotation);
		membershipMethod.getMethodInfo().addAttribute(membershipAttribute);
		// Membership parameters annotations
		ParameterAnnotationsAttribute membershipParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] membershipParamAnnotationsInfo = new Annotation[4][1];
		membershipParamAnnotationsInfo[0][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[1][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		membershipParamAnnotationsInfo[2][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationFilter), constPool));
		membershipParamAnnotationsInfo[3][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		membershipParamAnnotationsInfo[3][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		membershipParamAnnotations.setAnnotations(membershipParamAnnotationsInfo);
		membershipMethod.getMethodInfo().addAttribute(membershipParamAnnotations);
		
		// Add the method into the ensemble class
		ensembleClass.addMethod(membershipMethod);
		
		// Map method
		CtMethod mapMethod = CtNewMethod.make(String.format(
				"public static void map("
				+ "		String memberId,"
				+ "		deeco.metadata.MetadataWrapper coord%1$s,"
				+ "		cz.cuni.mff.d3s.deeco.task.ParamHolder member%1$s) {"
				+ " System.out.println(\"Knowledge injection (%1$s) for component \" + memberId);"
				+ "	member%1$s.value = coord%1$s; }",
					correlationSubject),
				ensembleClass);
		// KnowledgeExchange annotation for the map method
		Annotation mapAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange", constPool);
		AnnotationsAttribute mapAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		mapAttribute.addAnnotation(mapAnnotation);
		mapMethod.getMethodInfo().addAttribute(mapAttribute);
		// Map parameters annotations
		ParameterAnnotationsAttribute mapParamAnnotations = new ParameterAnnotationsAttribute(constPool,
				ParameterAnnotationsAttribute.visibleTag);
		Annotation[][] mapParamAnnotationsInfo = new Annotation[3][1];
		mapParamAnnotationsInfo[0][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		mapParamAnnotationsInfo[0][0].addMemberValue("value", new StringMemberValue("member.id", constPool));
		mapParamAnnotationsInfo[1][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.In", constPool);
		mapParamAnnotationsInfo[1][0].addMemberValue("value", new StringMemberValue(
				String.format("coord.%s", correlationSubject), constPool));
		mapParamAnnotationsInfo[2][0] = new Annotation("cz.cuni.mff.d3s.deeco.annotations.Out", constPool);
		mapParamAnnotationsInfo[2][0].addMemberValue("value", new StringMemberValue(
				String.format("member.%s", correlationSubject), constPool));
		mapParamAnnotations.setAnnotations(mapParamAnnotationsInfo);
		mapMethod.getMethodInfo().addAttribute(mapParamAnnotations);
		
		// Add the method into the ensemble class
		ensembleClass.addMethod(mapMethod);
		
		/* Code that uses a template class to modify ---- Obsolete */
		/*ClassPool classPool = ClassPool.getDefault();
		CtClass ensembleClass = classPool.get("CorrelationComponent.CorrelationEnsembleTemplate");
		ensembleClass.setName(String.format("Correlation_%s", knowledgeField));

		ClassFile classFile = ensembleClass.getClassFile();
		ConstPool constPool = classFile.getConstPool();
		
		CtMethod mapMethod = CtNewMethod.make("public void testPrint(String s) { System.out.println(\"map \" + s); }", ensembleClass);
		Annotation mapAnnotation = new Annotation("cz.cuni.mff.d3s.deeco.annotations.KnowledgeExchange", constPool);
		AnnotationsAttribute mapAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		mapAttribute.addAnnotation(mapAnnotation);
		mapMethod.getMethodInfo().addAttribute(mapAttribute);
		ensembleClass.addMethod(mapMethod);

		CtMethod membership = ensembleClass.getMethod("membership", "(Ljava/lang/String;Ljava/lang/Integer;)Z");

		if(membership != null){
		    
		    // -- Get the annotation
		    AttributeInfo parameterAttributeInfo = membership.getMethodInfo().getAttribute(ParameterAnnotationsAttribute.visibleTag);
		    ConstPool parameterConstPool = parameterAttributeInfo.getConstPool();
		    ParameterAnnotationsAttribute parameterAtrribute = ((ParameterAnnotationsAttribute) parameterAttributeInfo);
		    Annotation[][] paramArrays = parameterAtrribute.getAnnotations();
		    Annotation[] addAnno = paramArrays[0];
		    //-- Edit the annotation adding values
		    addAnno[0].addMemberValue("value", new StringMemberValue("This is the value of the annotation", parameterConstPool));
		    paramArrays[0] = addAnno;
		    parameterAtrribute.setAnnotations(paramArrays);
		} // TODO: else throw exception*/
		
		
		return ensembleClass.toClass();
	}

}
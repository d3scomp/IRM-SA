package irm.test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cz.cuni.mff.d3s.deeco.am.AdaptationManager;
import cz.cuni.mff.d3s.deeco.irm.Assumption;
import cz.cuni.mff.d3s.deeco.irm.ExchangeInvariant;
import cz.cuni.mff.d3s.deeco.irm.IRMPrimitive;
import cz.cuni.mff.d3s.deeco.irm.Invariant;
import cz.cuni.mff.d3s.deeco.irm.Operation;
import cz.cuni.mff.d3s.deeco.irm.OperationType;
import cz.cuni.mff.d3s.deeco.irm.ProcessInvariant;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.KnowledgeRepository;
import cz.cuni.mff.d3s.deeco.knowledge.RepositoryKnowledgeManager;
import cz.cuni.mff.d3s.deeco.knowledge.local.LocalKnowledgeRepository;
import cz.cuni.mff.d3s.deeco.monitor.MonitorProviderImpl;
import cz.cuni.mff.d3s.deeco.monitoring.MonitorProvider;
import cz.cuni.mff.d3s.deeco.path.grammar.ParseException;
import cz.cuni.mff.d3s.deeco.processor.ConditionParser;
import cz.cuni.mff.d3s.deeco.provider.InstanceRuntimeMetadataProvider;
import cz.cuni.mff.d3s.deeco.runtime.Runtime;
import cz.cuni.mff.d3s.deeco.runtime.model.BooleanCondition;
import cz.cuni.mff.d3s.deeco.scheduling.AdaptationRealTimeScheduler;

public class InvariantTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws ParseException {
		AdaptationManager am = new AdaptationManager();
		KnowledgeRepository kr = new LocalKnowledgeRepository();
		KnowledgeManager km = new RepositoryKnowledgeManager(kr);
		am.setKnowledgeManager(km);
		AdaptationRealTimeScheduler scheduler = new AdaptationRealTimeScheduler(
				km, am);
		kr.setTimeProvider(scheduler);

		List<String> roles = new LinkedList<>();
		roles.add("GM");
		roles.add("GL");
		List<BooleanCondition> assumptions = ConditionParser
				.parseBooleanConditions(Assumptions.class);
		MonitorProvider mp = new MonitorProviderImpl(km);
		mp.monitorAssumptions(assumptions);

		List<BooleanCondition> activeMonitors = ConditionParser
				.parseBooleanConditions(GroupMember.class);
		activeMonitors.addAll(ConditionParser
				.parseBooleanConditions(GroupMember.class));
		mp.monitorProcessInvariants(activeMonitors);

		// Construct FF tree
		// Leaves
		Assumption a6 = new Assumption("6", "", null, roles, mp);
		Assumption a2 = new Assumption("2", "", null, roles, mp);
		Assumption a7 = new Assumption("7", "", null, roles, mp);
		Assumption a15 = new Assumption("15", "", null, roles, mp);
		Assumption a16 = new Assumption("16", "", null, roles, mp);
		Assumption a17 = new Assumption("17", "", null, roles, mp);
		// Assumption a18 = new Assumption("18", "", null, roles, mp);
		Assumption a25 = new Assumption("25", "", null, roles, mp);
		Assumption a24 = new Assumption("24", "", null, roles, mp);
		Assumption a27 = new Assumption("27", "", null, roles, mp);
		Assumption a28 = new Assumption("28", "", null, roles, mp);
		// Assumption a31 = new Assumption("31", "", null, roles, mp);
		// Assumption a32 = new Assumption("32", "", null, roles, mp);

		ProcessInvariant p10 = new ProcessInvariant("10", "", "GL", mp);
		// ProcessInvariant p38 = new ProcessInvariant("38", "", "GM", mp);
		ProcessInvariant p20 = new ProcessInvariant("20", "", "GM", mp);
		ProcessInvariant p26 = new ProcessInvariant("26", "", "GM", mp);
		ProcessInvariant p21 = new ProcessInvariant("21", "", "GM", mp);
		ProcessInvariant p22 = new ProcessInvariant("22", "", "GM", mp);
		ProcessInvariant p29 = new ProcessInvariant("29", "", "GM", mp);
		ProcessInvariant p30 = new ProcessInvariant("30", "", "GM", mp);
		// ProcessInvariant p33 = new ProcessInvariant("33", "", "GM", mp);
		// ProcessInvariant p36 = new ProcessInvariant("36", "", "GM", mp);

		ExchangeInvariant e8 = new ExchangeInvariant("8", "", "GM", "GL", mp);
		ExchangeInvariant e12 = new ExchangeInvariant("12", "", "GL", "GM", mp);

		mp.monitorExchangeInvariant("8");
		mp.monitorExchangeInvariant("12");

		// Inner nodes

		Operation and4 = new Operation(OperationType.AND);
		and4.setChildren(Arrays.asList(new Assumption[] { a6 }));
		a6.setParents(Arrays.asList(new IRMPrimitive[] { and4 }));
		Operation and5 = new Operation(OperationType.AND);
		and5.setChildren(Arrays.asList(new Assumption[] { a7 }));
		a7.setParents(Arrays.asList(new IRMPrimitive[] { and5 }));
		Operation and12 = new Operation(OperationType.AND);
		and12.setChildren(Arrays.asList(new Assumption[] { a15 }));
		a15.setParents(Arrays.asList(new IRMPrimitive[] { and12 }));
		Operation and19 = new Operation(OperationType.AND);
		and19.setChildren(Arrays.asList(new Invariant[] { a25, p26 }));
		a25.setParents(Arrays.asList(new IRMPrimitive[] { and19 }));
		p26.setParents(Arrays.asList(new IRMPrimitive[] { and19 }));
		Operation and13 = new Operation(OperationType.AND);
		and13.setChildren(Arrays.asList(new Assumption[] { a16, a17 }));
		a16.setParents(Arrays.asList(new IRMPrimitive[] { and13 }));
		a17.setParents(Arrays.asList(new IRMPrimitive[] { and13 }));
		// Operation and14 = Operation.AND;
		// and14.setChildren(Arrays.asList(new Assumption[]{a18}));
		Operation and23l = new Operation(OperationType.AND);
		and23l.setChildren(Arrays.asList(new Invariant[] { a27, p29 }));
		a27.setParents(Arrays.asList(new IRMPrimitive[] { and23l }));
		p29.setParents(Arrays.asList(new IRMPrimitive[] { and23l }));
		Operation and23r = new Operation(OperationType.AND);
		and23r.setChildren(Arrays.asList(new Invariant[] { a28, p30 }));
		a28.setParents(Arrays.asList(new IRMPrimitive[] { and23r }));
		p30.setParents(Arrays.asList(new IRMPrimitive[] { and23r }));

		Assumption a4 = new Assumption("4", "", and4, roles, mp);
		and4.setParents(Arrays.asList(new IRMPrimitive[] { a4 }));
		Assumption a5 = new Assumption("5", "", and5, roles, mp);
		and5.setParents(Arrays.asList(new IRMPrimitive[] { a5 }));
		Assumption a12 = new Assumption("12", "", and12, roles, mp);
		and12.setParents(Arrays.asList(new IRMPrimitive[] { a12 }));
		Assumption a13 = new Assumption("13", "", and13, roles, mp);
		and13.setParents(Arrays.asList(new IRMPrimitive[] { a13 }));
		// Assumption a14 = new Assumption("14", "", and14, roles, mp);

		// Operation and37 = Operation.AND;
		// and37.setChildren(Arrays.asList(new Invariant[]{p38, a12}));
		Operation or23 = new Operation(OperationType.OR);
		or23.setChildren(Arrays.asList(new Operation[] { and23l, and23r }));
		and23l.setParents(Arrays.asList(new IRMPrimitive[] { or23 }));
		and23r.setParents(Arrays.asList(new IRMPrimitive[] { or23 }));
		Invariant i23 = new Invariant("23", "", or23, roles, mp);
		or23.setParents(Arrays.asList(new IRMPrimitive[] { i23 }));
		Operation and11m = new Operation(OperationType.AND);
		and11m.setChildren(Arrays
				.asList(new Invariant[] { i23, p22, p21, a13 }));
		i23.setParents(Arrays.asList(new IRMPrimitive[] { and11m }));
		p22.setParents(Arrays.asList(new IRMPrimitive[] { and11m }));
		a13.setParents(Arrays.asList(new IRMPrimitive[] { and11m }));
		Operation or19 = new Operation(OperationType.OR);
		or19.setChildren(Arrays.asList(new IRMPrimitive[] { and19, a24 }));
		and19.setParents(Arrays.asList(new IRMPrimitive[] { or19 }));
		a24.setParents(Arrays.asList(new IRMPrimitive[] { or19 }));
		Invariant i19 = new Invariant("19", "", or19, roles, mp);
		or19.setParents(Arrays.asList(new IRMPrimitive[] { i19 }));
		Operation and11l = new Operation(OperationType.AND);
		and11l.setChildren(Arrays
				.asList(new Invariant[] { a12, i19, p20, p21 }));
		a12.setParents(Arrays.asList(new IRMPrimitive[] { and11l }));
		i19.setParents(Arrays.asList(new IRMPrimitive[] { and11l }));
		p20.setParents(Arrays.asList(new IRMPrimitive[] { and11l }));
		p21.setParents(Arrays.asList(new IRMPrimitive[] { and11l, and11m }));
		i23.setParents(Arrays.asList(new IRMPrimitive[] { and11l, and11m }));
		Operation or11 = new Operation(OperationType.OR);
		or11.setChildren(Arrays.asList(new IRMPrimitive[] { and11l, and11m }));
		and11l.setParents(Arrays.asList(new IRMPrimitive[] { or11 }));
		and11m.setParents(Arrays.asList(new IRMPrimitive[] { or11 }));
		Invariant i11 = new Invariant("11", "", or11, roles, mp);
		or11.setParents(Arrays.asList(new IRMPrimitive[] { i11 }));
		Operation and9 = new Operation(OperationType.AND);
		and9.setChildren(Arrays.asList(new Invariant[] { i11, e12 }));
		i11.setParents(Arrays.asList(new IRMPrimitive[] { and9 }));
		e12.setParents(Arrays.asList(new IRMPrimitive[] { and9 }));
		Invariant i9 = new Invariant("9", "", and9, roles, mp);
		and9.setParents(Arrays.asList(new IRMPrimitive[] { i9 }));
		Operation and3l = new Operation(OperationType.AND);
		and3l.setChildren(Arrays.asList(new Invariant[] { a4, e8, i9, p10 }));
		a4.setParents(Arrays.asList(new IRMPrimitive[] { and3l }));
		e8.setParents(Arrays.asList(new IRMPrimitive[] { and3l }));
		Operation and3r = new Operation(OperationType.AND);
		and3r.setChildren(Arrays.asList(new Invariant[] { a5, i9, p10 }));
		a5.setParents(Arrays.asList(new IRMPrimitive[] { and3r }));
		i9.setParents(Arrays.asList(new IRMPrimitive[] { and3l, and3r }));
		p10.setParents(Arrays.asList(new IRMPrimitive[] { and3l, and3r }));
		Operation or3 = new Operation(OperationType.OR);
		or3.setChildren(Arrays.asList(new IRMPrimitive[] { and3l, and3r }));
		and3l.setParents(Arrays.asList(new IRMPrimitive[] { or3 }));
		and3r.setParents(Arrays.asList(new IRMPrimitive[] { or3 }));
		Invariant i3 = new Invariant("3", "", or3, roles, mp);
		or3.setParents(Arrays.asList(new IRMPrimitive[] { i3 }));
		Operation and1 = new Operation(OperationType.AND);
		and1.setChildren(Arrays.asList(new Invariant[] { a2, i3 }));
		a2.setParents(Arrays.asList(new IRMPrimitive[] { and1 }));
		i3.setParents(Arrays.asList(new IRMPrimitive[] { and1 }));
		Invariant i1 = new Invariant("1", "", and1, roles, mp);
		and1.setParents(Arrays.asList(new IRMPrimitive[] { i1 }));

		am.addIRMInvariant(i1);

		Runtime r = new Runtime(scheduler, km, mp, false);
		InstanceRuntimeMetadataProvider provider = new InstanceRuntimeMetadataProvider();
		provider.fromComponentInstance(new GroupMember("GM1", "GL1", System
				.currentTimeMillis()));
		provider.fromComponentInstance(new GroupLeader("GL1"));
		provider.fromEnsembleDefinition(SensorData.class);
		provider.fromEnsembleDefinition(GMInDanger.class);
		r.deployRuntimeMetadata(provider.getRuntimeMetadata());
		r.run();
	}
}

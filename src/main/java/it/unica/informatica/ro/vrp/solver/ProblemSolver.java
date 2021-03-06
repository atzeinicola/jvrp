package it.unica.informatica.ro.vrp.solver;

import it.unica.informatica.ro.vrp.problem.Problem;
import it.unica.informatica.ro.vrp.problem.Solution;
import it.unica.informatica.ro.vrp.solver.initializers.Initializer;
import it.unica.informatica.ro.vrp.solver.strategies.Strategy;

import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProblemSolver {
	
	private static Logger log = LoggerFactory.getLogger(ProblemSolver.class);
	
	private Strategy strategy;
	private Initializer init;
	

	//---------------------------- Constructor -------------------------------//

	public ProblemSolver(Initializer init, Strategy strategy) {
		this.init = init;
		this.strategy = strategy;
	}
	

	//----------------------------- Methods ----------------------------------//

	/**
	 * Solve the given problem using the <code>Initializer</code> to obtain an initial solution
	 * and at each step use the <code>Strategy</code> to minimize the solution's cost. The method
	 * stops when no improvements occur between two steps.
	 * @see Strategy
	 * @see Initializer
	 * @see Problem
	 * @param problem the {@link Problem} to be solved
	 * @return	the solution of the problem
	 */
	public Solution solve(Problem problem) {
		
		/*
		 * Check if problem is valid
		 */
		Validate.isTrue(problem.isValid(), "invalid problem. Check the parameters.");
		
		/*
		 * Get an initial solution that respect the problem constrains
		 */
		Solution solution = init.initialSolution(problem);
		
		Validate.isTrue(solution.isValid(), "invalid initial solution");
		
		
		double currentCost = 0;
		double cost = solution.cost(problem.getCostMatrix());
		
		log.debug("initial solution:\n{}", solution);
		log.debug("initial cost: {}", cost);
		
		
		/*
		 * Step
		 */
		int i=0;
		do {
			log.debug("*********************************");
			log.debug("iteration {}", i++);
			log.debug("cost {}", cost);
			currentCost = cost;
			
			strategy.minimize(solution);
			
			cost = solution.cost(problem.getCostMatrix());
			log.debug("newCost {}", cost);
		}
		while(cost<currentCost);
		
		return solution;
	}
	
}

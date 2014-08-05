package com.digitalgeko.mobileapptraining.dto.response;

import java.util.List;

public class GetCurrencyValuesResponse {
	
	protected List<VariableEnvelop> variables;
	
	public List<VariableEnvelop> getVariables() {
		return variables;
	}

	public void setVariables(List<VariableEnvelop> variables) {
		this.variables = variables;
	}

	public static class VariableEnvelop {
		protected Variable variable;

		public Variable getVariable() {
			return variable;
		}

		public void setVariable(Variable variable) {
			this.variable = variable;
		}
		
		@Override
		public String toString() {
			return getVariable().getDescripcion();
		}
	}
	
	public static class Variable {

	    protected int moneda;
	    protected String descripcion;
		public int getMoneda() {
			return moneda;
		}
		public void setMoneda(int moneda) {
			this.moneda = moneda;
		}
		public String getDescripcion() {
			return descripcion;
		}
		public void setDescripcion(String descripcion) {
			this.descripcion = descripcion;
		}
    }
}

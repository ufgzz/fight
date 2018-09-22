package com.oofgz.fight.dto.redis;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Country implements Serializable {

	private static final long serialVersionUID = -1L;

	@NonNull
	private String code;


	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Country country = (Country) o;

		return this.code.equals(country.code);
	}

	@Override
	public int hashCode() {
		return this.code.hashCode();
	}

}

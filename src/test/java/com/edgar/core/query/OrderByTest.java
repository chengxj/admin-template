package com.edgar.core.query;

import org.junit.Assert;
import org.junit.Test;

import com.edgar.core.repository.OrderBy;

public class OrderByTest {

	@Test
	public void testSort() {
		OrderBy orderBy = OrderBy.asc("a");
		OrderBy orderBy2 = OrderBy.desc("b");
		Assert.assertEquals(0, orderBy.compareTo(orderBy2));
		orderBy2 = OrderBy.desc("b", 1);
		Assert.assertEquals(-1, orderBy.compareTo(orderBy2));
		orderBy2 = OrderBy.asc("b", -1);
		Assert.assertEquals(1, orderBy.compareTo(orderBy2));
	}
}

package uy.edu.ude.sipro.ui.componentes;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.util.HtmlUtils;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.HtmlRenderer;

import uy.edu.ude.sipro.service.Fachada;

@SpringComponent
@Scope(value="prototype")
public class OrdersGrid extends Grid<Order> {

	@Autowired
	private Fachada fachada;

	public OrdersGrid() {
		addStyleName("orders-grid");
		setSizeFull();
		removeHeaderRow(0);

		// Add stylenames to rows
//		setStyleGenerator(OrdersGrid::getRowStyle);

		Column<Order, String> summaryColumn = addColumn(resultado -> {
			return resultado.getTituloProyecto();
		}, new HtmlRenderer()).setExpandRatio(1).setSortProperty("resultado.ordenResultado").setMinimumWidthFromContent(false);
		summaryColumn.setStyleGenerator(order -> "summary");
	}

	@PostConstruct
	protected void init() {
//		setDataProvider(dataProvider);
	}
}
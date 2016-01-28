package uk.co.keithsjohnson.jasperreports.report;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class CustomJRDataSource<T> implements JRDataSource {

	Iterator<T> it;
	Object currentElement;

	@Override
	public boolean next() throws JRException {
		if (it.hasNext()) {
			currentElement = it.next();
			return true;
		} else {
			return false;
		}

	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {

		BeanInfo info;
		try {
			info = Introspector.getBeanInfo(currentElement.getClass());
			for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
				if (pd.getName().equals(jrField.getName())) {
					Method reader = pd.getReadMethod();
					return reader.invoke(currentElement);
				}
			}
		} catch (Exception e) {
			throw new JRException(e);
		}

		throw new JRException("Field " + jrField.getName() + " error");
	}

	public CustomJRDataSource<T> initBy(List<T> list) {
		this.it = list.iterator();
		return this;
	}
}

package ch.rasc.wampspring.demo.various.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ch.rasc.wampspring.EventMessenger;
import ch.rasc.wampspring.annotation.WampCallListener;
import ch.rasc.wampspring.message.CallMessage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BookService {

	@Autowired
	private EventMessenger eventMessenger;

	@Autowired
	private ObjectMapper objectMapper;

	@WampCallListener("grid:read")
	public Collection<Book> bookRead(CallMessage callMessage, StoreReadRequest readRequest)
			throws Throwable {
		System.out.println("bookRead:" + callMessage.getWebSocketSessionId());

		List<Book> list = BookDb.list();

		if (StringUtils.hasText(readRequest.getSort())) {
			List<SortInfo> si = this.objectMapper.readValue(readRequest.getSort(),
					new TypeReference<List<SortInfo>>() {
						// nothing_here
					});
			readRequest.setSortInfo(si);

			Comparator<Book> comparator = PropertyComparatorFactory
					.createComparatorFromSorters(readRequest.getSortInfo());

			if (comparator != null) {
				list.sort(comparator);
			}
		}
		return list;
	}

	@WampCallListener("grid:create")
	public List<Book> bookCreate(CallMessage callMessage, List<Book> books) {
		System.out.println("bookCreate:" + callMessage.getWebSocketSessionId());

		List<Book> result = new ArrayList<>();
		for (Book book : books) {
			BookDb.create(book);
			result.add(book);
		}

		this.eventMessenger.sendToAllExcept("grid:oncreate", result,
				callMessage.getWebSocketSessionId());
		return result;
	}

	@WampCallListener("grid:update")
	public List<Book> bookUpdate(CallMessage callMessage, List<Book> books) {
		System.out.println("bookUpdate:" + callMessage.getWebSocketSessionId());

		List<Book> result = new ArrayList<>();
		for (Book book : books) {
			BookDb.update(book);
			result.add(book);
		}

		this.eventMessenger.sendToAllExcept("grid:onupdate", result,
				callMessage.getWebSocketSessionId());
		return result;
	}

	@WampCallListener("grid:destroy")
	public void bookDestroy(CallMessage callMessage, List<Book> books) throws Throwable {
		System.out.println("bookDestroy:" + callMessage.getWebSocketSessionId());
		for (Book book : books) {
			BookDb.delete(book);
		}
		this.eventMessenger.sendToAll("grid:ondestroy", books);
	}
}

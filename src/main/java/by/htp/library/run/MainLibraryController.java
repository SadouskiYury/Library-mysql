package by.htp.library.run;

import java.util.GregorianCalendar;
import java.util.List;

import by.htp.library.dao.BookDao;
import by.htp.library.dao.imple.BookDaoImple;
import by.htp.library.entity.Author;
import by.htp.library.entity.Book;

public class MainLibraryController {

	public static void main(String[] args) {
		BookDao dao = new BookDaoImple();
		Book book = dao.read(1);
		System.out.println(book.toString());
		System.out.println("__________________");
		List<Book> list = dao.list();
		for (Book s : list)
			System.out.println(s);

//		int res = dao.add(new Book("WARandPEACE",
//				new Author("Lev", "Nikolaevich", "Tolstoy", new GregorianCalendar(1872, 8, 9))));
//		System.out.println(res);
		 dao.add(new Book("Война и мир", new Author("Лев ", "Николаевич", "Толстой",
		 new GregorianCalendar(1872, 8, 9))));
		 dao.add(new Book("Война и мир", new Author("Михаил", "Афанасьевич", "Булгаков",
				 new GregorianCalendar(1891, 4, 15))));
//		dao.delete(list.get(list.size() - 1));
		dao.update(list.get(list.size() - 2), "МАСТЕРиМАРГАРИТА");
		System.out.println(dao.getMaxId("book"));
	}

}

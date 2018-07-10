package by.htp.library.dao.imple;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import by.htp.library.dao.BookDao;
import by.htp.library.entity.Author;
import by.htp.library.entity.Book;

public class BookDaoImple implements BookDao {
	private static final String INSERT_BOOK = "INSERT INTO book (title,id_author) VALUES(?,(SELECT id FROM author WHERE name=? AND midlename=? AND surname=? AND birthday=?));";
	private static final String UPDATE_BOOK = "UPDATE book SET book.title=? WHERE id=?";
	private static final String SELECT_BOOK = "SELECT * FROM book JOIN author ON book.id_author=author.id";
	private static final String DELETE_BOOK = "DELETE FROM book WHERE book.id=?";
	private static final String INSERT_AUTHOR = "INSERT INTO author (name, midlename,surname,birthday) SELECT  ?,?,?,? WHERE NOT EXISTS (SELECT 1 FROM author WHERE name=? AND midlename=? AND surname=? AND birthday=? );";
	private static final String SELECT_BOOK_BYID = "SELECT * FROM book,author WHERE book.id=? AND book.id_author=author.id;";

	@Override
	public Book read(int id) {
		Book book = null;
		try (PreparedStatement ps = ConnectionBD.conectionWithDB(SELECT_BOOK_BYID)) {
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				book = buildBook(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return book;
	}

	@Override
	public List<Book> list() {
		List<Book> list = new ArrayList<>();
		try (PreparedStatement ps = ConnectionBD.conectionWithDB(SELECT_BOOK)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(buildBook(rs));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	@Override
	public int add(Book book) {
		int result = 0;
		try (PreparedStatement ps = ConnectionBD.conectionWithDB(INSERT_BOOK);) {
			Date birthday = new Date(book.getAuthor().getBirthDate().getTimeInMillis());
			chekAuthor(book.getAuthor());
			ps.setString(1, book.getTitle());
			ps.setString(2, book.getAuthor().getName());
			ps.setString(3, book.getAuthor().getMidlenme());
			ps.setString(4, book.getAuthor().getSurname());
			ps.setDate(5, birthday);
			result = ps.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Impossible add new Book");
			e.printStackTrace();

		}

		return result;
	}

	@Override
	public void delete(Book book) {
		try (PreparedStatement ps = ConnectionBD.conectionWithDB(DELETE_BOOK)) {
			ps.setInt(1, book.getId_book());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Book book, String newTitle) {
		try (PreparedStatement ps = ConnectionBD.conectionWithDB(UPDATE_BOOK)) {
			ps.setString(1, newTitle);
			ps.setInt(2, book.getId_book());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Book buildBook(ResultSet rs) throws SQLException {
		Book book = new Book();
		book.setTitle(rs.getString("title").trim());
		book.setId_book(rs.getInt("id"));
		book.setAuthor(buildAuthor(rs));
		return book;
	}

	private Author buildAuthor(ResultSet rs) throws SQLException {
		Author author = new Author();
		GregorianCalendar birthday = new GregorianCalendar();
		birthday.setTime(rs.getDate("birthday"));
		author.setName(rs.getString("name").trim());
		author.setMidlenme(rs.getString("midlename").trim());
		author.setSurname(rs.getString("surname").trim());
		author.setBirthDate(birthday);
		author.setId(rs.getInt("author.id"));
		return author;
	}

	private int chekAuthor(Author author) throws SQLException {
		Date birthday = new Date(author.getBirthDate().getTimeInMillis());
		PreparedStatement ps = ConnectionBD.conectionWithDB(INSERT_AUTHOR);
		ps.setString(1, author.getName());
		ps.setString(2, author.getMidlenme());
		ps.setString(3, author.getSurname());
		ps.setDate(4, birthday);
		ps.setString(5, author.getName());
		ps.setString(6, author.getMidlenme());
		ps.setString(7, author.getSurname());
		ps.setDate(8, birthday);
		int result = ps.executeUpdate();

		return result;
	}

	public int getMaxId(String table) {
		int max = 0;
		try (PreparedStatement ps = ConnectionBD.conectionWithDB("SELECT MAX(id) FROM " + table)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				max = rs.getInt(1);
		} catch (SQLException e) {

			e.printStackTrace();
		}

		return max;

	}

}

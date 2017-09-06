package org.hibernate.tutorial;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.Session;
import org.hibernate.tutorial.domain.Event;
import org.hibernate.tutorial.domain.Person;
import org.hibernate.tutorial.domain.SysRegion;
import org.hibernate.tutorial.util.HibernateUtil;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

public class EventManager {

	 public static void main(String[] args) throws IOException {
	        EventManager mgr = new EventManager();

	        //if (args[0].equals("store")) {
	            //mgr.createAndStoreEvent("My Event5", new Date());
	        //}

	           
	            /*List events = mgr.listEvents();
	            for (int i = 0; i < events.size(); i++) {
	                Event theEvent = (Event) events.get(i);
	                Set<Person> pSet = theEvent.getParticipants();
	                for (Person p : pSet){
		                System.out.println(
		                        "Event: " + theEvent.getTitle() + " Time: " + theEvent.getDate()+" Person: "+ p.getId()
		                );
	                }
	            }*/
	           // mgr.createAmdStorePerson(20,"Michle","Jondan");
	            //mgr.updatePerson(1L);
	            //mgr.addPersonToEvent(2L,4L);
	            //mgr.addEmailToPerson(1L, "qab@163.com");
	        //mgr.testLoadPerson(new Long(2));
	        
	        /*Long eventId = mgr.createAndStoreEvent0("My Event", new Date());
            Long personId = mgr.createAndStorePerson0(39,"Foo", "Bar");
            mgr.addPersonToEvent(personId, eventId);
            System.out.println("Added person " + personId + " to event " + eventId);*/
	        //mgr.testRegion();
	        mgr.grabRegionData();
	        HibernateUtil.getSessionFactory().close();
	    }

	    private void createAndStoreEvent(String title, Date theDate) {
	        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Event theEvent = new Event();
	        theEvent.setTitle(title);
	        theEvent.setDate(theDate);
	        session.save(theEvent);

	        session.getTransaction().commit();
	    }
	    
	    private Long createAndStoreEvent0(String title, Date theDate) {
	        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Event theEvent = new Event();
	        theEvent.setTitle(title);
	        theEvent.setDate(theDate);
	        session.save(theEvent);

	        session.getTransaction().commit();
	        return theEvent.getId();
	    }
	    
	    private List listEvents() {
	        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();
	        List result = session.createQuery("from Event").list();
	        session.getTransaction().commit();
	        return result;
	    }
	    private void createAndStorePerson(Integer age, String firstname, String lastname){
	    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    	session.beginTransaction();
	    	
	    	Person thePerson = new Person();
	    	thePerson.setAge(age);
	    	thePerson.setFirstname(firstname);
	    	thePerson.setLastname(lastname);
	    	session.save(thePerson);
	    	
	    	session.getTransaction().commit();
	    }
	    
	    private Long createAndStorePerson0(Integer age, String firstname, String lastname){
	    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	    	session.beginTransaction();
	    	
	    	Person thePerson = new Person();
	    	thePerson.setAge(age);
	    	thePerson.setFirstname(firstname);
	    	thePerson.setLastname(lastname);
	    	session.save(thePerson);
	    	
	    	session.getTransaction().commit();
	    	
	    	return thePerson.getId();
	    }
	    private void updatePerson(Long personId){
	    	 Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	         session.beginTransaction();
	    	 Person aPerson = (Person) session.load(Person.class, personId);
	    	 aPerson.setFirstname("Michael");
	    	 aPerson.setLastname("Jordan");
	    	 session.update(aPerson);
	    	 
	    	 session.getTransaction().commit();
	    }
	    private void addPersonToEvent0(Long personId, Long eventId) {
	        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Person aPerson = (Person) session.load(Person.class, personId);
	        Event anEvent = (Event) session.load(Event.class, eventId);
	        aPerson.getEvents().add(anEvent);

	        session.getTransaction().commit();
	    }
	    
	    private void addPersonToEvent(Long personId, Long eventId) {
	        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Person aPerson = (Person) session
	                .createQuery("select p from Person p left join fetch p.events where p.id = :pid")
	                .setParameter("pid", personId)
	                .uniqueResult(); // Eager fetch the collection so we can use it detached
	        Event anEvent = (Event) session.load(Event.class, eventId);

	        session.getTransaction().commit();

	        // End of first unit of work

	        aPerson.getEvents().add(anEvent); // aPerson (and its collection) is detached

	        // Begin second unit of work

	        Session session2 = HibernateUtil.getSessionFactory().getCurrentSession();
	        session2.beginTransaction();
	        session2.update(aPerson); // Reattachment of aPerson

	        session2.getTransaction().commit();
	    }
	    
	    private void addEmailToPerson(Long personId, String emailAddress) {
	        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Person aPerson = (Person) session.load(Person.class, personId);
	        // adding to the emailAddress collection might trigger a lazy load of the collection
	        aPerson.getEmailAddresses().add(emailAddress);

	        session.getTransaction().commit();
	    }
	    
	    private void testLoadPerson(Long personId) {
	    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();

	        Person aPerson = (Person) session.load(Person.class, personId);
	        // adding to the emailAddress collection might trigger a lazy load of the collection

	        Set<Event> events = aPerson.getEvents();
	        for (Event e: events){
	           System.out.println("Person "+aPerson.getId()+" Event "+e.getId());
	        }
	        session.getTransaction().commit();
	    }
	    
	    private void testRegion(){
	    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();
	        
	        SysRegion region = new SysRegion();
	        region.setName("北京市");
	        region.setDistrictCode("110000");
	        
	        Set<SysRegion> child = new HashSet<SysRegion>();
	        
	        SysRegion r1 = new SysRegion();
	        r1.setName("市辖区");
	        r1.setDistrictCode("110100");
	        r1.setParent(region);
	        
	        SysRegion r2 = new SysRegion();
	        r2.setName("东城区");
	        r2.setDistrictCode("110101");
	        r2.setParent(region);
	        
	        child.add(r1);
	        child.add(r2);
	        region.setChildren(child);
	        
	        
	        session.save(region);
	        
	       /* SysRegion sysRegion = (SysRegion) session.get(SysRegion.class, 2);
	        System.out.println(sysRegion.getParent().getName());*/
	        session.getTransaction().commit();
	    }
	    
	    
	    private void grabRegionData() throws IOException{
	    	Session session = HibernateUtil.getSessionFactory().getCurrentSession();
	        session.beginTransaction();
	        
	    	URL url = new URL("http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/201703/t20170310_1471429.html");
	    	//File in = new File("C:\\Users\\Administrator\\Desktop\\xzqhbm.html");
	    	//Document doc = Jsoup.parse(in, "utf-8");
			Document doc = Jsoup.parse(url, 30000);
			Element body = doc.body();
			Elements ps = body.select("p.MsoNormal");
			Elements els = null;
			Element el = null;
			String str = "";
			SysRegion region = null;
			Set<SysRegion> children = null;
			SysRegion child = null;
			boolean isChildEnd = false;
			region = new SysRegion();
			//children = new HashSet<SysRegion>();
			children = new TreeSet<SysRegion>();
			
			Set<SysRegion> childChildren = null;
			SysRegion subChild = null;
			for (Element p : ps){
				//省、直辖市、自治区节点
				if (p.child(0).tag().getName().equalsIgnoreCase("b") || p.child(1).tag().getName().equalsIgnoreCase("b")){
					els = p.select("span");
					if (isChildEnd) {
						region.setChildren(children);
						session.save(region);
						isChildEnd = false;
						region = new SysRegion();
						//children = new HashSet<SysRegion>();
						children = new TreeSet<SysRegion>();
					}
					for (Element e: els){
						if (e.hasAttr("lang")) { //编码
							str = e.ownText().replaceAll(Jsoup.parse("&nbsp;").text(), " ").replaceAll("\\s*", "").trim();
							if (!StringUtil.isBlank(str)){
								region.setDistrictCode(str);
						    }
						} else { //地区名称
							str = e.ownText().replaceAll(Jsoup.parse("&nbsp;").text(), " ").replaceAll("\\s*", "").trim();
							if (!StringUtil.isBlank(str)){
								region.setName(str);
						    }
						}
					}
					if (p.nextElementSibling() != null && 
							(p.nextElementSibling().child(0).tag().getName().equalsIgnoreCase("b")|| 
									p.nextElementSibling().child(1).tag().getName().equalsIgnoreCase("b"))){
						isChildEnd = true;
					}
					//最后一个地区
					if (p.nextElementSibling() == null){
						region.setChildren(children);
						session.save(region);
					}
				} else { //地级市、直辖市下的区、县等
					els = p.getElementsByTag("span");
					el = els.get(1);
					if (el.hasAttr("lang")) { //编码
						str = el.ownText().replaceAll(Jsoup.parse("&nbsp;").text(), " ").replaceAll("\\s*", "").trim();
						if (str.endsWith("00")){  //地市级节点
							child = new SysRegion();
							childChildren =  new TreeSet<SysRegion>();
							if (!StringUtil.isBlank(str)){
								child.setDistrictCode(str);
						    }
							el = el.nextElementSibling();//下一兄弟节点，地区名称
							str = el.ownText().replaceAll(Jsoup.parse("&nbsp;").text(), " ").replaceAll("\\s*", "").trim();
							if (!StringUtil.isBlank(str)){
								child.setName(str);
						    }
							child.setParent(region);
							child.setChildren(childChildren);
							children.add(child);
						} else {  //区县级节点
							subChild =  new SysRegion();
							if (!StringUtil.isBlank(str)){
								subChild.setDistrictCode(str);
						    }
							el = el.nextElementSibling();//下一兄弟节点，地区名称
							str = el.ownText().replaceAll(Jsoup.parse("&nbsp;").text(), " ").replaceAll("\\s*", "").trim();
							if (!StringUtil.isBlank(str)){
								subChild.setName(str);
						    }
							subChild.setParent(child);
							childChildren.add(subChild);
						}
						
					}
					//省级地区结束
					if(p.nextElementSibling().child(0).tag().getName().equalsIgnoreCase("b")
							||p.nextElementSibling().child(1).tag().getName().equalsIgnoreCase("b")){
						isChildEnd = true;
					}
				}
			}
			session.getTransaction().commit();
	    }
}

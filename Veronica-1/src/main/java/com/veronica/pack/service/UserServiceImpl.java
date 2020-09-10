package com.veronica.pack.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.veronica.pack.favDto.FavoriteSongDto;
import com.veronica.pack.model.FavoriteSong;
import com.veronica.pack.model.History;
import com.veronica.pack.model.Music;
import com.veronica.pack.model.OTP;
import com.veronica.pack.model.PlayList;
import com.veronica.pack.model.PlayListSongs;
import com.veronica.pack.model.Role;
import com.veronica.pack.model.User;
import com.veronica.pack.repository.FavoriteSongRepository;
import com.veronica.pack.repository.HistoryRepository;
import com.veronica.pack.repository.MusicRepository;
import com.veronica.pack.repository.OTPRepo;
import com.veronica.pack.repository.PlayListRepo;
import com.veronica.pack.repository.PlayListSongRepo;
import com.veronica.pack.repository.UserRepository;


@Service
public class UserServiceImpl implements  UserDetailsService{
	@Autowired
	private  UserRepository userrepository;
	@Autowired
	private OTPRepo otprepo;
	private String otp;
	//private String emailid;
	private String name;
	private String psw;
	//private String cpsw;
	private JavaMailSender javaMailSender;
	@Autowired
	public UserServiceImpl(JavaMailSender javaMailSender)
	 {
		 this.javaMailSender=javaMailSender;
	 }
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private MusicRepository musicrepository;
	@Autowired
	private FavoriteSongRepository favoriteSongRepo;
	@Autowired
	private HistoryRepository historyrepo;
	@Autowired
	private PlayListRepo playlistrepo;
	@Autowired
	private PlayListSongRepo playlistsongrepo;
	public String verifyPassword(String name,String emailid,String psw,String cpsw ) {
		//this.emailid=emailid;
		 this.name=name;
		 this.psw=psw;
		// this.cpsw=cpsw;
		 if(cpsw.equals(psw))
		 {
			 User user= userrepository.findByEmailid(emailid);
			 if(user==null)
			 {
			
			 Random random = new Random();
			 otp=String.valueOf(random.nextInt(9999-0000)+0000);
			 try {
			 SimpleMailMessage mail=new SimpleMailMessage();	
			 mail.setTo(emailid);
			 mail.setFrom("murugesanmouli@gmail.com");
			 mail.setSubject("OTP from musicstremer");
			 mail.setText(otp);
			 javaMailSender.send(mail);
			 OTP objotp=new OTP(emailid,otp);
			OTP checkotp= otprepo.findByEmailid(emailid);
			if(checkotp!=null)
			{
				otprepo.deleteById(checkotp.getId());
			}
			 otprepo.save(objotp);
			 
			 return "OTP";
			 }
			 catch(Exception e)
			 {
				 return "redirect:/registration?error0";
			 }
			 }
			 else
			 {
				 return "redirect:/registration?error1";
			 }
		 }
		 else {
			 return "redirect:/registration?error2";
	 }
	
	}
	public String verifyOtps(String emailid ,String userotp)
	{
		OTP checkotp= otprepo.findByEmailid(emailid);
		if(checkotp==null)
		{
			 return "redirect:/OTP?error";
		}
		else
		{
		if(checkotp.getOtp().equals(userotp))
		{
			User user=new User(name,emailid,passwordEncoder.encode(psw),Arrays.asList(new Role("ROLE_USER")));

			 userrepository.save(user);
			 otprepo.deleteById(checkotp.getId());
			return "redirect:/login?success";
			
		}
		else
		{
			 return "redirect:/OTP?error";
		}
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user= userrepository.findByEmailid(username);
		if(user==null)
		{
			throw new UsernameNotFoundException("Invalid username or password");
			
		}
		return new org.springframework.security.core.userdetails.User(user.getEmailid(),user.getPassword(),mapRolesToAuthorities(user.getRoles()));
	}
	 private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
	        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	    }
	public  User findUser(String email) {
		User user= userrepository.findByEmailid(email);
		return user;
	}
	 public Optional<Music> getFile(Long fileId)
	 {
		 return musicrepository.findById(fileId);
	 }
	 public List<Music> getFiles()
	 {
		 return (List<Music>) musicrepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
	 }

	public Music saveFile(MultipartFile file,String language,String genres)
	{
		 String docname=file.getOriginalFilename();
		 try
		 {
			 Music mymusic=new Music(docname,file.getContentType(),language,genres,file.getBytes());
			 return musicrepository.save(mymusic);
		 }
		 catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		 return null;
	}
	public void userSelectedFavoriteSong(Long userid,Long songid)
	{
		FavoriteSong favSong=favoriteSongRepo.findByuseridAndfindBysongid(userid,songid);
		if(favSong==null)
		{
			FavoriteSong favs=new FavoriteSong(userid,songid);
			favoriteSongRepo.save(favs);
		}
		else
		{
			favoriteSongRepo.deleteById(favSong.getId());
		}
		
	}
	public List<FavoriteSongDto> getUserFav(Long userid) {
		// TODO Auto-generated method stub
		List<FavoriteSongDto> favdto=new ArrayList<FavoriteSongDto>();
		List<FavoriteSong> lst=favoriteSongRepo.findByUserId(userid);
		for(FavoriteSong fav:lst)
		{
			Music music=musicrepository.findByID(fav.getSongid());
			if(music!=null) {
			FavoriteSongDto favDto=new FavoriteSongDto(fav.getId(),music.getName(),music.getLanguage());
			favdto.add(favDto);
			}
			
		}
		return favdto;
		
	}
	public List<Music> getFavorite(Long userid) {
		// TODO Auto-generated method stub
		List<Music> music=new ArrayList<Music>();
		List<FavoriteSong> lst=favoriteSongRepo.findByUserId(userid);
		for(FavoriteSong fav:lst)
		{
			Music musictemp=musicrepository.findByID(fav.getSongid());
			if(musictemp!=null)
			{music.add( musictemp);}
			
		}
		return music;
		
	}
	public List<Music> getUserLanguage(String language) {
		// TODO Auto-generated method stub
		List<Music> music=musicrepository.findByLanguage(language);
		return music;
	}
	public void storeHistory(Long userid, String songName) {
		Music mus=musicrepository.findByName(songName);
		Long songid=mus.getId();
		History savehis=new History(userid,songid);
		// TODO Auto-generated method stub
		History his=historyrepo.findByuseridAndfindBysongid(userid, songid);
		if(his!=null)
		{

			historyrepo.deleteById(his.getId());
			
			historyrepo.save(savehis);
			
		}
		else
		{
		List<History> lst=historyrepo.findByUserId(userid);
		if(lst.size()>15)
		{
			List<Long> idlst=new ArrayList<Long>();
			for( History history:lst)
			{
				idlst.add(history.getId());
			}
			Long minid=Collections.min(idlst);
			historyrepo.deleteById(minid);
			historyrepo.save(savehis);
		}
		else
		{
			historyrepo.save(savehis);
		}
	}
	}
	public List<Music> getUserHistory(Long userid) {
		List<History> his= historyrepo.findByUserId(userid);
		List<Music> historyMusic=new ArrayList<Music>();
		for(History hs:his)
		{
			
			Music mus=musicrepository.findByID(hs.getSongid());
			if(mus!=null)
			{
			historyMusic.add(mus);
			}
		}
		Collections.reverse(historyMusic);
		return historyMusic;
	}
	public List<Music> getgenres(String genres) {
		List<Music> music=musicrepository.findByGenres(genres);
		return music;
	}
	public String removeSongs(Long id) {
		musicrepository.deleteById(id);
		return "redirect:/uploadsongs";
	}
	public List<PlayList> getPlayList(Long userid) {
		List<PlayList> playlist=playlistrepo.findByUserId(userid);
		return playlist;
	}
	public String addPlayList(String playlistname, Long userid) {
		PlayList pl=new PlayList(userid,playlistname);
		playlistrepo.save(pl);
		return "redirect:/player";
	}
	public List<Music> getPlayListSongs(Long id) {
		
		List<PlayListSongs>  pls=playlistsongrepo.findByPlayListId(id);
		List<Music> PlayListMusic=new ArrayList<Music>();
		for(PlayListSongs pl:pls)
		{
			Music mus=musicrepository.findByID(pl.getSongid());
			if(mus!=null)
			{
			PlayListMusic.add(mus);
			}
		}
		return PlayListMusic;
	}
	public PlayList getplname(Long id) {
	return playlistrepo.findByid(id);
		
	}
	public void addtoplaylist(Long plid, Long songid) {
		PlayListSongs checkpl=playlistsongrepo.findByPlaylistidAndSongId(plid,songid);
		if(checkpl==null)
		{
		PlayListSongs pls=new PlayListSongs(plid,songid);
		playlistsongrepo.save(pls);
		}
	}
	public void removefromplaylist(Long plid, Long songid) {
		PlayListSongs pls=playlistsongrepo.findByPlaylistidAndSongId(plid,songid);
		
		playlistsongrepo.deleteById(pls.getId());
		
		
	}
	public String deleteplaylist(Long plid) {
		
		playlistrepo.deleteById(plid);
		return "redirect:/player";
	}
	

	

}


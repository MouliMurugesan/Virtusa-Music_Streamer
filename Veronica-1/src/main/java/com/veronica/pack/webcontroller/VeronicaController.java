package com.veronica.pack.webcontroller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.veronica.pack.favDto.FavoriteSongDto;
import com.veronica.pack.model.Music;
import com.veronica.pack.model.PlayList;
import com.veronica.pack.model.User;
import com.veronica.pack.service.UserServiceImpl;

@Controller
public class VeronicaController {
	@Autowired
	private UserServiceImpl userServiceImpl;
@GetMapping("/registration")
public String showRegistrationform()
{
	return "registration";
}
@GetMapping("/login")
public String showLogin()
{
	return "login";
}

@PostMapping("/registration")
public String registerUserAcount( @RequestParam("name") String name,@RequestParam("emailid") String emailid, @RequestParam("password") String psw,@RequestParam("confirmpassword") String cpsw)
{
	return userServiceImpl.verifyPassword(name,emailid,psw,cpsw);

}
@GetMapping("/OTP")
public String otp()
{
	return "OTP";
}
@PostMapping("/OTP")
public String getOtp(@RequestParam("emailid") String emailid,@RequestParam("otp")String userotp)
{
	return userServiceImpl.verifyOtps(emailid,userotp);
	
}
@GetMapping("/uploadsongs")
public String music (Model model)
{
	List<Music> music=userServiceImpl.getFiles();
	model.addAttribute("player", music);
	return "uploadsongs";
}
@PostMapping("/uploadsongs")
public String uploadFiles(@RequestParam("files") MultipartFile[] files, @RequestParam("language") String language,@RequestParam("genres")String genres)
{
	for(MultipartFile file:files)
	{
		userServiceImpl.saveFile(file,language,genres);
	}
	return "redirect:/uploadsongs";
}
@GetMapping("/getallmusic/{id}")
public ResponseEntity<ByteArrayResource> play(@PathVariable Long id)
{
	Music music=userServiceImpl.getFile(id).get();
	return ResponseEntity.ok().contentType(MediaType.parseMediaType(music.getType()))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment:filename=\""+music.getName()+"\"")
			.body(new ByteArrayResource(music.getData()));
}
@GetMapping("/player")
public String player (Model model,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	List<Music> music=userServiceImpl.getFiles();
	List<PlayList> playlist=userServiceImpl.getPlayList(userid);
	model.addAttribute("playlist",playlist);
	model.addAttribute("player", music);
	model.addAttribute("listtitle","All Songs");
	model.addAttribute("pagetitle","Home");
	return "player";
}
@GetMapping(value="/userFavorite/{songid}" ,produces=MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(value = HttpStatus.OK)
public void setUserFavorite(@PathVariable Long songid,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	userServiceImpl.userSelectedFavoriteSong(userid,songid);
}
@GetMapping(value="/getuserfav")
public ResponseEntity<Object>  getUserFavorite(Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	List<FavoriteSongDto> lst= userServiceImpl.getUserFav(userid);
	return new ResponseEntity<Object>(lst,HttpStatus.OK);
}
@GetMapping(value="/favorite")
public String  showFavorite(Model model,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	List<Music> music=userServiceImpl.getFavorite(userid);
	model.addAttribute("player", music);
	List<PlayList> playlist=userServiceImpl.getPlayList(userid);
	model.addAttribute("playlist",playlist);
	model.addAttribute("listtitle","Favourite Songs");
	model.addAttribute("pagetitle","Favourite Songs");
	return "player";
	
}
@GetMapping("/userprofile")
public String favoriteSongOnly(Model model,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	model.addAttribute("user", user);
	return "userprofile";
}
@GetMapping("/getlanguage/{language}")
public String  getUserLanguage(Model model,@PathVariable String language,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	List<Music> lst= userServiceImpl.getUserLanguage(language);
	model.addAttribute("player", lst);
	List<PlayList> playlist=userServiceImpl.getPlayList(userid);
	model.addAttribute("playlist",playlist);
	model.addAttribute("listtitle",language+" Songs");
	model.addAttribute("pagetitle",language+" Songs");
	return "player";
}
@GetMapping("/song")
public ResponseEntity<Object> song()
{
	List<Music> lst= userServiceImpl.getFiles();
	return new ResponseEntity<Object>(lst,HttpStatus.OK);
}

@GetMapping(value="/storehistory/{songName}",produces=MediaType.APPLICATION_JSON_VALUE)
 @ResponseStatus(value = HttpStatus.OK)
public void history(@PathVariable String songName,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	userServiceImpl.storeHistory(userid, songName);
}
@GetMapping("/userHistory")
public String  getUserHistory( Model model,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	List<Music> hislst=userServiceImpl.getUserHistory(userid);
	model.addAttribute("player",hislst);
	List<PlayList> playlist=userServiceImpl.getPlayList(userid);
	model.addAttribute("playlist",playlist);
	model.addAttribute("listtitle","Recently Played Songs");

	return "History";
}
@GetMapping("/userGenres/{genres}")
public String getGenres( Model model,@PathVariable String genres,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	List<Music> usergenres=userServiceImpl.getgenres(genres);
	model.addAttribute("player",usergenres);
	List<PlayList> playlist=userServiceImpl.getPlayList(userid);
	model.addAttribute("playlist",playlist);
	model.addAttribute("listtitle",genres+" Songs");
	model.addAttribute("pagetitle",genres+" Songs");
	return "player";
	
	
}
@GetMapping("/removeSong/{id}")
public String removeSong(@PathVariable Long id)
{
	return userServiceImpl.removeSongs(id);
}
@PostMapping("/createplaylist")
public String createPlayList(@RequestParam("playlistname") String playlistname,Principal principal)
{
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	return userServiceImpl.addPlayList(playlistname,userid);
	
}
@GetMapping("/playlist/{id}")
public String getPlayListSong(@PathVariable Long id,Model model,Principal principal)
{
	List<Music> music=userServiceImpl.getPlayListSongs(id);
	String email=principal.getName();
	User user=userServiceImpl.findUser(email);
	Long userid=user.getId();
	List<PlayList> playlist=userServiceImpl.getPlayList(userid);
	model.addAttribute("playlist",playlist);
	model.addAttribute("player", music);
	PlayList pl= userServiceImpl.getplname(id);
	model.addAttribute("listtitle",pl.getPlayListName()+" Songs");
	model.addAttribute("selectedplaylist",pl.getPlayListName()+" Songs");
	model.addAttribute("playlistid",id);
	
	return "PlayList";	
}
@GetMapping(value="/addtoplatlist/{songid}/{plid}" ,produces=MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(value = HttpStatus.OK)
public void addtoplaylist (@PathVariable Long songid,@PathVariable Long plid,Principal principal)
{
	
	userServiceImpl.addtoplaylist(plid,songid);
}
@GetMapping("/removefromplaylist/{songid}/{plid}")
public RedirectView removefromplaylist(@PathVariable Long songid,@PathVariable Long plid,Model model,Principal principal)
{
	 userServiceImpl.removefromplaylist(plid,songid);
	 
		
		RedirectView rv = new RedirectView();
        rv.setContextRelative(true);
        rv.setUrl("/playlist/{plid}");
        return rv;
}
@GetMapping("/deleteplaylist/{plid}")
public String deleteplaylist(@PathVariable Long plid )
{
	return userServiceImpl.deleteplaylist(plid);
}
}

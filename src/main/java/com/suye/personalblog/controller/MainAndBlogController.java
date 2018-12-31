package com.suye.personalblog.controller;

import com.suye.personalblog.model.*;
import com.suye.personalblog.service.*;
import com.suye.personalblog.tool.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 * Author: Suyeq
 * Date: 2018-12-20
 * Time: 21:27
 */
@Controller
public class MainAndBlogController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ColumnService columnService;
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private ConmentService conmentService;
    @Autowired
    private ConmentMessageConversion conmentMessageConversion;
    @Autowired
    private BlogMessageConversion blogMessageConversion;
    @Autowired
    private ArchiveMessageConversion archiveMessageConversion;
    @Autowired
    private PaginationTool paginationTool;

    @RequestMapping("/")
    public String root(){
        return "redirect:/index";
    }

    @RequestMapping("/index")
    public ModelAndView allMessage(@RequestParam(name = "search",required = false)String searchContent,
                                   Model model){
        System.out.println("lall");
        paginationTool.reset();
        List<BlogMessageConversion.BlogMessage> recentBlogList=null;
        if (searchContent!=null){
            paginationTool.setCategorySearch();
            paginationTool.setContent(searchContent);
            recentBlogList= blogMessageConversion.getBlogMessageList(blogService.searchContent(searchContent));
        }else {
            paginationTool.setCategoryAll();
            recentBlogList= blogMessageConversion.getBlogMessageList(blogService.recentBlogs());
        }
        RunningTrackStack.clearRunningTrackStack();
        int blogTotal=blogService.blogTotal();
        List<Label> labelList=labelService.getAllLabels();
        List<Category> categoryList=categoryService.getAllCategory();
        List<Column> columnList=columnService.getAllColumn();
        List<BlogMessageConversion.BlogMessage> popularBlogList=
                blogMessageConversion.getBlogMessageList(blogService.mostPopularBlog());
        List<ConmentMessageConversion.ConmentMessage> recentConmentList=
                conmentMessageConversion.conmentMessageList(conmentService.recentConment());
        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("labelList",labelList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("columnList",columnList);
        model.addAttribute("popularBlogList",popularBlogList);
        model.addAttribute("recentConmentList",recentConmentList);
        model.addAttribute("recentBlogList",recentBlogList);
        if (searchContent!=null){
            return new ModelAndView("front/searchshow","allMessge",model);
        }
        return new ModelAndView("index","allMessge",model);
    }

    /**
     * 响应博客的分类
     * @param model
     * @return
     */
    @GetMapping("/blogs")
    public ModelAndView indexBlog(Model model){
        System.out.println("blogs");
        paginationTool.reset();
        paginationTool.setCategoryBlog();
        RunningTrackStack.addRunningStack(0,"博客","/blogs");
        int blogTotal=blogService.blogTotal();
        List<Label> labelList=labelService.getAllLabels();
        List<Category> categoryList=categoryService.getAllCategory();
        List<Column> columnList=columnService.getAllColumn();
        List<BlogMessageConversion.BlogMessage> popularBlogList=
                blogMessageConversion.getBlogMessageList(blogService.mostPopularBlog());

        List<ConmentMessageConversion.ConmentMessage> recentConmentList=
                conmentMessageConversion.conmentMessageList(conmentService.recentConment());

        List<BlogMessageConversion.BlogMessage> recentBlogList=
                blogMessageConversion.getBlogMessageList(blogService.recentBlogsNotShuoShuo());
        model.addAttribute("recentBlogList",recentBlogList);
        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("labelList",labelList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("columnList",columnList);
        model.addAttribute("popularBlogList",popularBlogList);
        model.addAttribute("recentConmentList",recentConmentList);
        return new ModelAndView("front/indexblog","allMessge",model);

    }

    /**
     * 响应说说的分类
     * @param model
     * @return
     */
    @GetMapping("/shuoshuos")
    public ModelAndView indexShuoShuo(Model model){
        System.out.println("shuosshuos");
        paginationTool.reset();
        paginationTool.setCategoryShuoShuo();
        RunningTrackStack.addRunningStack(1,"说说","/shuoshuos");
        int blogTotal=blogService.blogTotal();
        List<Label> labelList=labelService.getAllLabels();
        List<Category> categoryList=categoryService.getAllCategory();
        List<Column> columnList=columnService.getAllColumn();
        List<BlogMessageConversion.BlogMessage> popularBlogList=
                blogMessageConversion.getBlogMessageList(blogService.mostPopularBlog());
        List<ConmentMessageConversion.ConmentMessage> recentConmentList=
                conmentMessageConversion.conmentMessageList(conmentService.recentConment());

        List<BlogMessageConversion.BlogMessage> recentBlogList=
                blogMessageConversion.getBlogMessageList(blogService.recentBlogsIsShuoShuo());
        model.addAttribute("recentBlogList",recentBlogList);
        model.addAttribute("recentBlogList",recentBlogList);
        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("labelList",labelList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("columnList",columnList);
        model.addAttribute("popularBlogList",popularBlogList);
        return new ModelAndView("front/indexshuoshuo","allMessge",model);
    }

    /**
     * 响应加载更多的全部
     * @param model
     * @return
     */
    @GetMapping("/loadMoreAll")
    public ModelAndView loadMoreAll(Model model){
        System.out.println("loadMoreAll");
//        List<BlogMessageConversion.BlogMessage> recentBlogList=
//                blogMessageConversion.getBlogMessageList(blogService.loadMoreRecentBlogs(loadMoreAll*7));
        List<BlogMessageConversion.BlogMessage> recentBlogList=
                blogMessageConversion.getBlogMessageList(paginationTool.blogList());
        model.addAttribute("recentBlogList",recentBlogList);
        return new ModelAndView("index","allMessge",model);
    }

    /**
     * 响应加载更多的博客
     * @param model
     * @return
     */
    @GetMapping("/loadMoreBlog")
    public ModelAndView loadMoreBlog(Model model){
        System.out.println("loadMoreBlog");
        //loadMoreAll=1;
        //loadMoreShuoShuo=1;
//        List<BlogMessageConversion.BlogMessage> recentBlogList=
//                blogMessageConversion.getBlogMessageList(blogService.loadMoreRecentNotShuoShuo(loadMoreBlog*7));
//
        List<BlogMessageConversion.BlogMessage> recentBlogList=
                blogMessageConversion.getBlogMessageList(paginationTool.blogList());

        //loadMoreBlog++;
        model.addAttribute("recentBlogList",recentBlogList);
        return new ModelAndView("front/indexblog","allMessge",model);
    }

    /**
     * 响应加载更多的说说
     * @param model
     * @return
     */
    @GetMapping("/loadMoreShuoShuo")
    public ModelAndView loadMoreShuoShuo(Model model){
        System.out.println("loadMoreShuoShuo");
//        loadMoreAll=1;
//        loadMoreBlog=1;
        List<BlogMessageConversion.BlogMessage> recentBlogList=
                blogMessageConversion.getBlogMessageList(paginationTool.blogList());

        //loadMoreShuoShuo++;
        model.addAttribute("recentBlogList",recentBlogList);
        return new ModelAndView("front/indexshuoshuo","allMessge",model);
    }

    @GetMapping("/loadMoreSearch")
    public ModelAndView  loadMoreSearch(Model model){
        System.out.println("loadMoreSearch");
        List<BlogMessageConversion.BlogMessage> recentBlogList=
                blogMessageConversion.getBlogMessageList(paginationTool.blogList());
        model.addAttribute("recentBlogList",recentBlogList);
        return new ModelAndView("front/searchshow","allMessage",model);
    }

    @RequestMapping("/blogDetails/{blogid}")
    public ModelAndView showBlogDetails(@PathVariable("blogid")int blogId, Model model){
        System.out.println("blogdatails");
        int blogTotal=blogService.blogTotal();
        blogService.increaseReadNum(blogId);
        List<Label> labelList=labelService.getAllLabels();
        List<Category> categoryList=categoryService.getAllCategory();
        List<Column> columnList=columnService.getAllColumn();
        List<BlogMessageConversion.BlogMessage> popularBlogList=
                blogMessageConversion.getBlogMessageList(blogService.mostPopularBlog());
        List<ConmentMessageConversion.ConmentMessage> recentConmentList=
                conmentMessageConversion.conmentMessageList(conmentService.recentConment());
        BlogMessageConversion.BlogMessage blogMessage=
                blogMessageConversion.getOneBlogMessage(blogService.findOneById(blogId));
        List<RunningTrackStack.RunningTrack> runningTrackList=RunningTrackStack.getRunningTrackStack();

        List<ConmentMessageConversion.ConmentMessage> recentAllConmentList=
                conmentMessageConversion.findAllConmentsByBlogId(blogId,0);
        List<Object> conmentPages=conmentMessageConversion.conversionTotal(conmentService.conmnetTotal(blogId));

        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("labelList",labelList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("columnList",columnList);
        model.addAttribute("popularBlogList",popularBlogList);
        model.addAttribute("recentConmentList",recentConmentList);
        model.addAttribute("blogDatailsMessage",blogMessage);
        model.addAttribute("runningTrackList",runningTrackList);
        model.addAttribute("recentAllConmentList",recentAllConmentList);
        model.addAttribute("conmentPages",conmentPages);
        return new ModelAndView("front/blogDetailsPage","blogMessage",model);
    }

    @RequestMapping("/archive")
    public ModelAndView showarchivePage(Model model){
        System.out.println("archive");
        blogService.increaseReadNum(10);
        int blogTotal=blogService.blogTotal();
        List<Label> labelList=labelService.getAllLabels();
        List<Category> categoryList=categoryService.getAllCategory();
        List<Column> columnList=columnService.getAllColumn();
        List<BlogMessageConversion.BlogMessage> popularBlogList=
                blogMessageConversion.getBlogMessageList(blogService.mostPopularBlog());
        List<ConmentMessageConversion.ConmentMessage> recentConmentList=
                conmentMessageConversion.conmentMessageList(conmentService.recentConment());
        BlogMessageConversion.BlogMessage blogMessage=
                blogMessageConversion.getOneBlogMessage(blogService.findArchive());
        List<ArchiveMessageConversion.ArchiveMessage> archiveMessageList=
                archiveMessageConversion.archiveMessageList(blogService.findAllBlogTimeDesc());
        List<RunningTrackStack.RunningTrack> runningTrackList=RunningTrackStack.getRunningTrackStack();
        model.addAttribute("blogMessage",blogMessage);
        model.addAttribute("archiveMessageList",archiveMessageList);
        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("labelList",labelList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("columnList",columnList);
        model.addAttribute("popularBlogList",popularBlogList);
        model.addAttribute("recentConmentList",recentConmentList);
        model.addAttribute("runningTrackList",runningTrackList);
        return new ModelAndView("front/archivePage","archivePage",model);
    }

    @RequestMapping("/friend")
    public  ModelAndView showFriendChain(Model model){
        int blogTotal=blogService.blogTotal();
        List<Visitor> friendList=visitorService.findAllFriends();
        List<Label> labelList=labelService.getAllLabels();
        List<Category> categoryList=categoryService.getAllCategory();
        List<Column> columnList=columnService.getAllColumn();
        List<BlogMessageConversion.BlogMessage> popularBlogList=
                blogMessageConversion.getBlogMessageList(blogService.mostPopularBlog());
        List<ConmentMessageConversion.ConmentMessage> recentConmentList=
                conmentMessageConversion.conmentMessageList(conmentService.recentConment());
        BlogMessageConversion.BlogMessage blogMessage=
                blogMessageConversion.getOneBlogMessage(blogService.findFirends());

        List<ConmentMessageConversion.ConmentMessage> recentAllConmentList=
                conmentMessageConversion.findAllConmentsByBlogId(11,0);

        List<Object> conmentPages=conmentMessageConversion.conversionTotal(conmentService.conmnetTotal(11));
        model.addAttribute("blogMessage",blogMessage);
        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("labelList",labelList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("columnList",columnList);
        model.addAttribute("popularBlogList",popularBlogList);
        model.addAttribute("recentConmentList",recentConmentList);
        model.addAttribute("friendList",friendList);
        model.addAttribute("recentAllConmentList",recentAllConmentList);
        model.addAttribute("conmentPages",conmentPages);
        return new ModelAndView("front/friendChain","friendChain",model);
    }

    @RequestMapping("/aboutme")
    public  ModelAndView showAboutMe(Model model){
        blogService.increaseReadNum(12);
        int blogTotal=blogService.blogTotal();
        List<Label> labelList=labelService.getAllLabels();
        List<Category> categoryList=categoryService.getAllCategory();
        List<Column> columnList=columnService.getAllColumn();
        List<BlogMessageConversion.BlogMessage> popularBlogList=
                blogMessageConversion.getBlogMessageList(blogService.mostPopularBlog());
        List<ConmentMessageConversion.ConmentMessage> recentConmentList=
                conmentMessageConversion.conmentMessageList(conmentService.recentConment());
        BlogMessageConversion.BlogMessage blogMessage=
                blogMessageConversion.getOneBlogMessage(blogService.findAboutMe());

        List<ConmentMessageConversion.ConmentMessage> recentAllConmentList=
                conmentMessageConversion.findAllConmentsByBlogId(12,0);

        List<Object> conmentPages=conmentMessageConversion.conversionTotal(conmentService.conmnetTotal(12));
        model.addAttribute("blogMessage",blogMessage);
        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("labelList",labelList);
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("columnList",columnList);
        model.addAttribute("popularBlogList",popularBlogList);
        model.addAttribute("recentConmentList",recentConmentList);
        model.addAttribute("recentAllConmentList",recentAllConmentList);
        model.addAttribute("conmentPages",conmentPages);
        return new ModelAndView("front/aboutMe","aboutMe",model);
    }

}

package com.example.socialfood.controller.Navigation;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import android.content.Context;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.test.core.app.ApplicationProvider;

import com.example.socialfood.R;
import com.example.socialfood.camera.CameraFragment;
import com.example.socialfood.controller.Post.PostController;
import com.example.socialfood.controller.User.UserController;
import com.example.socialfood.gui.fragments.Map.MapFragment;
import com.example.socialfood.gui.fragments.PostCreationFragment;
import com.example.socialfood.gui.fragments.ProfileFragment;
import com.example.socialfood.gui.fragments.SettingsFragment;
import com.example.socialfood.model.entities.Post;
import com.example.socialfood.model.entities.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains unit tests to verify the functionality of the {@link com.example.socialfood.controller.Navigation.NavigationController},
 */
public class NavigationControllerTest {

    private com.example.socialfood.controller.Navigation.NavigationController navigationController;
    private FragmentManager mockFragmentManager;
    private FragmentTransaction mockTransaction;
    private PostController mockPostController;
    private UserController mockUserController;
    private BottomNavigationView mockBottomNavigation;
    private Context mockContext;

    /**
     * Sets up the test environment before each test.
     */
    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        mockFragmentManager = mock(FragmentManager.class);
        mockTransaction = mock(FragmentTransaction.class);
        mockPostController = mock(PostController.class);
        mockUserController = mock(UserController.class);
        mockBottomNavigation = mock(BottomNavigationView.class);
        when(mockFragmentManager.beginTransaction()).thenReturn(mockTransaction);
        when(mockTransaction.replace(anyInt(), any(Fragment.class))).thenReturn(mockTransaction);
        when(mockTransaction.addToBackStack(null)).thenReturn(mockTransaction);

        navigationController = new com.example.socialfood.controller.Navigation.NavigationController(
                context,
                mockFragmentManager,
                mockPostController,
                mockUserController,
                mockBottomNavigation
        );
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Navigation.NavigationController#showProfile(int)} method.
     * <p>
     * Verifies that the correct {@link ProfileFragment} is displayed when a user's profile is shown.
     */
    @Test
    public void testShowProfile() {
        int userId = 1;
        User mockUser = new User();
        List<Post> mockPosts = new ArrayList<>();

        when(mockUserController.getUserById(userId)).thenReturn(mockUser);
        when(mockPostController.getPostsFromUser(userId)).thenReturn(mockPosts);

        navigationController.showProfile(userId);

        verify(mockFragmentManager.beginTransaction()).replace(eq(R.id.fragment_container), any(ProfileFragment.class));
        verify(mockTransaction).commit();
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Navigation.NavigationController#showSettings()} method.
     * <p>
     * Verifies that the correct {@link SettingsFragment} is displayed when navigating to settings.
     */
    @Test
    public void testShowSettings() {
        navigationController.showSettings();

        verify(mockFragmentManager.beginTransaction()).replace(eq(R.id.fragment_container), any(SettingsFragment.class));
        verify(mockTransaction).commit();
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Navigation.NavigationController#showMap()} method.
     * <p>
     * Verifies that the correct {@link MapFragment} is displayed when navigating to the map.
     */
    @Test
    public void testShowMap() {
        List<Post> mockPosts = new ArrayList<>();
        when(mockPostController.getAllPosts()).thenReturn(mockPosts);

        navigationController.showMap();

        verify(mockFragmentManager.beginTransaction()).replace(eq(R.id.fragment_container), any(MapFragment.class));
        verify(mockTransaction).commit();
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Navigation.NavigationController#showCamera()} method.
     * <p>
     * Verifies that the correct {@link CameraFragment} is displayed when navigating to the camera.
     */
    @Test
    public void testShowCamera() {
        navigationController.showCamera();

        verify(mockFragmentManager.beginTransaction()).replace(eq(R.id.fragment_container), any(CameraFragment.class));
        verify(mockTransaction).commit();
    }

    /**
     * Tests the navigation item selection handling.
     * <p>
     * Verifies that selecting the map navigation item results in the correct {@link MapFragment}
     * being displayed.
     */
    @Test
    public void testHandleNavigationItemSelected() {
        MenuItem mockMenuItem = mock(MenuItem.class);
        when(mockMenuItem.getItemId()).thenReturn(R.id.nav_map);

        ArgumentCaptor<BottomNavigationView.OnNavigationItemSelectedListener> captor =
                ArgumentCaptor.forClass(BottomNavigationView.OnNavigationItemSelectedListener.class);
        verify(mockBottomNavigation).setOnNavigationItemSelectedListener(captor.capture());

        BottomNavigationView.OnNavigationItemSelectedListener listener = captor.getValue();
        boolean result = listener.onNavigationItemSelected(mockMenuItem);

        assertTrue(result);
        verify(mockFragmentManager.beginTransaction()).replace(eq(R.id.fragment_container), any(MapFragment.class));
        verify(mockTransaction).commit();
    }

    /**
     * Tests the {@link com.example.socialfood.controller.Navigation.NavigationController#showPostDetail(Post)} method.
     * <p>
     * Verifies that the correct {@link com.example.socialfood.gui.fragments.PostDetailFragment}
     * is displayed when navigating to a post detail view.
     */
    @Test
    public void testShowPostDetail() {
        Post mockPost = new Post();
        when(mockPostController.getPostById(anyInt(), anyInt())).thenReturn(mockPost);

        navigationController.showPostDetail(mockPost);

        verify(mockFragmentManager.beginTransaction()).replace(eq(R.id.fragment_container), any());
        verify(mockTransaction).commit();
    }


    /**
     * Tests the {@link com.example.socialfood.controller.Navigation.NavigationController#onPhotoTaken(String)} method.
     * <p>
     * Verifies that a photo path triggers the {@link PostCreationFragment} to be displayed.
     */
    @Test
    public void testOnPhotoTaken() {
        String photoPath = "/path/to/photo";
        navigationController.onPhotoTaken(photoPath);

        verify(mockFragmentManager.beginTransaction()).replace(eq(R.id.fragment_container), any(PostCreationFragment.class));
        verify(mockTransaction).commit();
    }
}

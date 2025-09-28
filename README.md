# Room Rental Android App

A native Android application built with Kotlin that consumes the Room Rental API service. This app allows users to browse available properties, view property details, and make booking requests.

## Features

### Authentication
- User registration with role selection (Tenant/Landlord)
- User login with JWT token authentication
- Secure token storage using SharedPreferences

### Property Management
- Browse available properties
- Search properties by city
- View detailed property information including:
  - Property images
  - Price, bedrooms, bathrooms, area
  - Amenities
  - Description
  - Property type

### Booking System
- Date selection for booking periods
- Booking request submission
- Real-time availability checking

### UI/UX Features
- Material Design 3 components
- Responsive layouts with RecyclerView
- Swipe-to-refresh functionality
- Loading states and error handling
- Collapsing toolbar for property details

## Architecture

The app follows modern Android development practices:

- **MVVM Architecture**: Clear separation of concerns
- **Repository Pattern**: Centralized data management
- **Retrofit**: REST API consumption
- **Coroutines**: Asynchronous programming
- **ViewBinding**: Type-safe view references
- **Material Components**: Modern UI design

## Project Structure

```
app/src/main/java/com/roomrental/android/
├── data/
│   ├── api/           # API service interfaces and client
│   ├── model/         # Data models and DTOs
│   └── repository/    # Repository implementations
├── ui/
│   ├── auth/          # Authentication screens
│   ├── property/      # Property-related screens
│   └── MainActivity   # Main property listing
└── utils/             # Utility classes
```

## Setup Instructions

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 24+ (minimum)
- Java 8 or Kotlin 1.9+

### Local Development Setup

1. **Start the Backend Services**
   ```bash
   cd /path/to/room-rental-app
   ./start-services.sh
   ```

2. **Configure Network Access**
   - The app is configured to use `10.0.2.2:8080` (Android emulator localhost mapping)
   - For physical devices, update the `BASE_URL` in `ApiClient.kt` to your local IP address

3. **Open in Android Studio**
   - Open the `room-rental-android` folder in Android Studio
   - Wait for Gradle sync to complete
   - Ensure all dependencies are downloaded

4. **Run the App**
   - Select an emulator or connected device
   - Click "Run" or use `Ctrl+R` / `Cmd+R`

### API Configuration

The app connects to your local Room Rental API:
- **Base URL**: `http://10.0.2.2:8080/` (for emulator)
- **API Gateway**: Port 8080
- **Authentication**: JWT tokens stored securely

### Network Security

The app includes network security configuration to allow cleartext traffic to localhost for development:
- `10.0.2.2` (Android emulator localhost)
- `localhost`
- `127.0.0.1`

## Key Components

### Authentication Flow
1. Login/Register screens with validation
2. JWT token storage in encrypted SharedPreferences
3. Automatic token injection in API requests
4. Session management with logout functionality

### Property Browsing
1. Main screen displays available properties
2. Search functionality by city
3. Pull-to-refresh for updated data
4. Property cards with essential information

### Property Details
1. Collapsing toolbar with property image
2. Comprehensive property information
3. Date picker for booking periods
4. Booking request submission

### Data Layer
1. Retrofit API service definitions
2. Repository pattern with Result wrapper
3. Coroutines for asynchronous operations
4. Error handling and loading states

## Dependencies

### Core Android
- AndroidX Core KTX
- AppCompat
- Material Design Components
- ConstraintLayout
- Lifecycle (ViewModel, LiveData)
- Navigation Components

### Networking
- Retrofit 2.9.0
- OkHttp Logging Interceptor
- Gson Converter

### UI/Image Loading
- Glide 4.16.0
- RecyclerView
- SwipeRefreshLayout

### Coroutines
- Kotlinx Coroutines Android

## Usage

### First Time Setup
1. Launch the app
2. Create an account (select Tenant for browsing/booking)
3. Login with your credentials

### Browsing Properties
1. Main screen shows all available properties
2. Use search bar to filter by city
3. Pull down to refresh property list
4. Tap any property to view details

### Making a Booking
1. Open property details
2. Select start and end dates
3. Tap "Book Property"
4. Booking request will be submitted for landlord approval

### Navigation
- Use toolbar menu to access "My Bookings" (coming soon)
- Use "Logout" to clear session and return to login

## Development Notes

### For Emulator
- Uses `10.0.2.2` to access host machine's localhost
- No additional network configuration needed

### For Physical Device
1. Ensure device and development machine are on same network
2. Update `ApiClient.kt` BASE_URL to your machine's IP address:
   ```kotlin
   private const val BASE_URL = "http://192.168.1.XXX:8080/"
   ```

### API Integration
- All API endpoints from your backend are integrated
- Authentication, Properties, and Bookings services
- Proper error handling and user feedback

## Future Enhancements

- My Bookings screen implementation
- Property image gallery
- Map integration for property locations
- Push notifications for booking updates
- Offline caching capabilities
- Property filtering by price, type, amenities

## Troubleshooting

### Common Issues

1. **Network Connection Errors**
   - Ensure backend services are running
   - Check IP address configuration for physical devices
   - Verify network security config allows cleartext traffic

2. **Build Errors**
   - Clean and rebuild project
   - Invalidate caches and restart Android Studio
   - Check Gradle sync completion

3. **Authentication Issues**
   - Clear app data to reset stored tokens
   - Verify API endpoints are responding
   - Check network connectivity

The app is now ready for development and testing with your local Room Rental API!
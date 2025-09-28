# Custom Dialog Usage Guide

## Overview
The `CustomDialog` utility provides a beautiful, consistent way to show dialogs throughout the FlexiRent app with a modern Material Design 3 appearance.

## Features
- ✨ **Modern Design**: Material 3 components with rounded corners and proper elevation
- 🎨 **Themed**: Automatically uses app's green color scheme
- 🔄 **Multiple Types**: Success, Error, Warning, and Info dialogs
- 🎯 **Easy to Use**: Simple static methods for common scenarios
- 🛠️ **Flexible**: Builder pattern for custom configurations

## Quick Usage Examples

### 1. Success Dialog
```kotlin
CustomDialog.showSuccess(
    context = this,
    title = "Booking Confirmed!",
    message = "Your property booking has been successfully submitted.",
    buttonText = "Great!"
) {
    // Optional callback when user clicks OK
    finish()
}
```

### 2. Error Dialog
```kotlin
CustomDialog.showError(
    context = this,
    title = "Login Failed",
    message = "Invalid username or password. Please try again."
)
```

### 3. Warning Dialog
```kotlin
CustomDialog.showWarning(
    context = this,
    title = "Network Issue",
    message = "Please check your internet connection and try again."
)
```

### 4. Info Dialog
```kotlin
CustomDialog.showInfo(
    context = this,
    title = "App Update",
    message = "A new version of FlexiRent is available in the app store."
)
```

### 5. Confirmation Dialog
```kotlin
CustomDialog.showConfirmation(
    context = this,
    title = "Cancel Booking",
    message = "Are you sure you want to cancel this booking? This action cannot be undone.",
    positiveText = "Yes, Cancel",
    negativeText = "Keep Booking",
    positiveCallback = {
        // User confirmed cancellation
        cancelBooking()
    },
    negativeCallback = {
        // User chose to keep booking (optional)
    }
)
```

## Advanced Usage with Builder Pattern

### Custom Dialog with Builder
```kotlin
val dialog = CustomDialog.Builder(this)
    .setTitle("Custom Title")
    .setMessage("This is a custom message with specific configuration")
    .setType(CustomDialog.DialogType.WARNING)
    .setPositiveButton("Continue") {
        // Handle positive action
    }
    .setNegativeButton("Cancel") {
        // Handle negative action
    }
    .setCancelable(false) // Prevent dismissing by tapping outside
    .show()
```

## Dialog Types and Icons

| Type | Icon | Use Case |
|------|------|----------|
| **SUCCESS** | ✅ Green checkmark | Successful operations, confirmations |
| **ERROR** | ❌ Red X | Errors, failures, critical issues |
| **WARNING** | ⚠️ Orange triangle | Warnings, confirmations, cautions |
| **INFO** | ℹ️ Blue info | Information, tips, updates |

## Integration Examples in FlexiRent

### Property Booking Success
```kotlin
// In PropertyDetailActivity after successful booking
CustomDialog.showSuccess(
    this,
    "Booking Requested!",
    "Your booking request has been sent to the landlord. You'll receive a notification once it's reviewed."
) {
    finish() // Return to property list
}
```

### Network Error
```kotlin
// In MainActivity when API fails
CustomDialog.showError(
    this,
    "Connection Error",
    "Unable to load properties. Please check your internet connection and try again."
)
```

### Logout Confirmation
```kotlin
// In MainActivity when user clicks logout
CustomDialog.showConfirmation(
    this,
    "Sign Out",
    "Are you sure you want to sign out of FlexiRent?",
    positiveText = "Sign Out",
    negativeText = "Stay",
    positiveCallback = {
        authRepository.logout()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
)
```

### Property Deletion Warning
```kotlin
// For landlords deleting their properties
CustomDialog.showConfirmation(
    this,
    "Delete Property",
    "Are you sure you want to delete this property? This action cannot be undone and will cancel all pending bookings.",
    positiveText = "Delete",
    negativeText = "Cancel",
    positiveCallback = {
        deleteProperty(propertyId)
    }
)
```

## Customization Options

### Builder Methods
- `setTitle(String)` - Set dialog title
- `setMessage(String)` - Set dialog message
- `setType(DialogType)` - Set icon and theme
- `setPositiveButton(String, callback)` - Configure primary button
- `setNegativeButton(String, callback)` - Configure secondary button
- `setCancelable(Boolean)` - Allow/prevent dismissal by tapping outside

### Styling
The dialog automatically inherits the app's theme colors:
- **Primary Color**: Green (#4CAF50) for success and primary buttons
- **Error Color**: Red (#F44336) for error dialogs
- **Warning Color**: Orange (#FF9800) for warning dialogs
- **Background**: White with subtle shadows

## Best Practices

1. **Use Appropriate Types**: Match dialog type to the situation
2. **Clear Messages**: Keep messages concise and actionable
3. **Consistent Wording**: Use similar button text across the app
4. **Handle Callbacks**: Always provide appropriate actions for button clicks
5. **Don't Overuse**: Use for important messages, not minor feedback

## Migration from Toast

### Before (Toast)
```kotlin
Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show()
```

### After (Custom Dialog)
```kotlin
CustomDialog.showError(this, "Login Failed", "Invalid credentials. Please try again.")
```

The custom dialog provides better user experience with:
- More prominent display
- Better readability
- Consistent styling
- User acknowledgment required
- Optional callbacks for actions
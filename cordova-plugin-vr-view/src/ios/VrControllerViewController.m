//
//  VrControllerViewController.m
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 08/06/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import "VrControllerViewController.h"
#import <objc/runtime.h>

@interface VrControllerViewController ()

@end

@implementation VrControllerViewController

-(void)setOptions: (NSMutableDictionary *) options {
    startDisplayMode = [[options objectForKey:@"startDisplayMode"] intValue];
}

- (void)viewDidLoad {
    [super viewDidLoad];

    _activityIndicator = [[UIActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhiteLarge];

    _activityIndicator.hidesWhenStopped = NO;
    _activityIndicator.autoresizingMask = UIViewAutoresizingNone;

    UIImage *playImage = [UIImage imageNamed:@"play.png"];
    _playIndicator = [[UIImageView alloc] initWithImage:playImage];
    _playIndicator.frame = CGRectMake(0, 0, 74, 74);
    _playIndicator.hidden = YES;
    [_activityIndicator startAnimating];
    overlayIndicatorAdded = NO;

    _errorLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 214, 21)];
    _errorLabel.backgroundColor = [UIColor blackColor];
    _errorLabel.textColor = [UIColor whiteColor];
    _errorLabel.hidden = YES;
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)showPlayIndicatorState: (BOOL) state {
    _playIndicator.hidden = !state;
}

- (void)widgetView:(GVRWidgetView *)widgetView didLoadContent:(id)content {
    [_activityIndicator stopAnimating];
    _activityIndicator.hidden = YES;
    [self showPlayIndicatorState:NO];
}

- (void)widgetView:(GVRWidgetView *)widgetView didFailToLoadContent:(id)content withErrorMessage:(NSString *)errorMessage {
    NSLog(@"Failed to load video: %@", errorMessage);
    [self showError: @"Error while loading resource"];
}

- (void)widgetView:(GVRWidgetView *)widgetView didChangeDisplayMode:(GVRWidgetDisplayMode)displayMode {
    if(displayMode == kGVRWidgetDisplayModeEmbedded) {
        [cardboardButton removeObserver:self forKeyPath:@"frame"];
        [self dismissViewControllerAnimated:YES completion:NULL];
    }
    if(displayMode == kGVRWidgetDisplayModeFullscreen || displayMode == kGVRWidgetDisplayModeFullscreenVR) {
        if(overlayIndicatorAdded == NO) {
            [self getOverlayView];
            [overlayView addSubview:_activityIndicator];
            [overlayView addSubview:_playIndicator];
            [overlayView addSubview:_errorLabel];
            overlayIndicatorAdded = YES;
        }
        [self centerOverlayView];
    }
}

-(void)getOverlayView {
    @autoreleasepool {
        unsigned int numberOfProperties = 0;
        objc_property_t *propertyArray = class_copyPropertyList([_widgetView superclass], &numberOfProperties);

        id _gVROverlayViewController = [_widgetView valueForKey:@"fullscreenController"];
        overlayView = [_gVROverlayViewController valueForKey:@"overlayView"];

        backButton = [overlayView valueForKey:@"backButton"];
        cardboardButton = [overlayView valueForKey:@"cardboardButton"];

        [cardboardButton addObserver:self forKeyPath:@"frame" options:NSKeyValueObservingOptionNew context:nil];

        free(propertyArray);
    }
}

-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context {
    [self centerOverlayView];
}

-(void)centerOverlayView {
    float xActivity = (cardboardButton.center.x - backButton.center.x) / 2.0 + backButton.center.x;
    float yActivity = (cardboardButton.center.y - backButton.center.y) / 2.0 + backButton.center.y;
    CGPoint center = CGPointMake(xActivity, yActivity);

    _activityIndicator.center = center;
    _playIndicator.center = center;
    _errorLabel.center = center;
}

-(void)showError: (NSString *) errorLabelText {
    _errorLabel.text = errorLabelText;
    _errorLabel.hidden = NO;
    _activityIndicator.hidden = YES;
    [self showPlayIndicatorState:NO];
}

@end

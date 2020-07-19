//
//  VrControllerViewController.h
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 08/06/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GVROverlayView.h"
#import "GVRWidgetView.h"

@interface VrControllerViewController : UIViewController <GVRWidgetViewDelegate> {
    UIActivityIndicatorView *_activityIndicator;
    UIImageView *_playIndicator;
    GVRWidgetView *_widgetView;
    UILabel *_errorLabel;

    GVROverlayView *overlayView;
    UIButton *backButton;
    UIButton *cardboardButton;
    BOOL overlayIndicatorAdded;

    NSInteger startDisplayMode;
}

-(void)showPlayIndicatorState: (BOOL) state;
-(void)showError: (NSString *) errorLabelText;
-(void)setOptions: (NSMutableDictionary *) options;

@end

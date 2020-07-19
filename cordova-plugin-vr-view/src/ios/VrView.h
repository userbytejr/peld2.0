//
//  VrView.h
//  GoogleVrViewTest
//
//  Created by Leonardo Tonghini on 12/05/17.
//  Copyright © 2017 Leonardo Tonghini. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <Cordova/CDVPlugin.h>

@interface VrView : CDVPlugin

- (void)playVideo:(CDVInvokedUrlCommand *)command;

@end
